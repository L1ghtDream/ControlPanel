package dev.lightdream.common.dto.permission;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.GlobalPermissionContainer;
import dev.lightdream.common.database.Permission;
import dev.lightdream.common.database.User;
import dev.lightdream.databasemanager.dto.entry.impl.StringDatabaseEntry;

import java.util.List;

public abstract class PermissionContainer extends StringDatabaseEntry {

    public PermissionContainer() {
        super(CommonMain.instance);
    }

    /**
     * @param identifier Unique identifier for the server / node / global in the format
     *                   {@link PermissionEnum.PermissionType}_{@link #id}
     * @return The server / node / global context of the identifier
     */
    public static PermissionContainer getByIdentifier(String identifier) {
        PermissionEnum.PermissionType type = PermissionEnum.PermissionType.valueOf(identifier.split("_")[0]);
        String id = identifier.split("_")[1];

        PermissionContainer target = null;

        switch (type) {
            case SERVER:
                target = CommonMain.instance.getDatabaseManager().getServer(id);
                break;
            case GLOBAL:
                target = GlobalPermissionContainer.getInstance();
                break;
        }

        return target;

    }

    public List<Permission> gerUserPermissions(User user) {
        return CommonMain.instance.getDatabaseManager().getPermissions(user, this);
    }

    public boolean hasPermission(User user, PermissionEnum permission) {
        if (permission.getType().equals(PermissionEnum.PermissionType.SERVER) && gerUserPermissions(user).stream().anyMatch(p -> p.permission.equals(PermissionEnum.GLOBAL_ADMIN))) {
            return true;
        }
        return gerUserPermissions(user).stream().anyMatch(p -> p.permission.equals(permission));
    }

    public void addPermission(User user, PermissionEnum permissionEnum) {
        if (hasPermission(user, permissionEnum)) {
            return;
        }
        new Permission(user, permissionEnum, this).save();
    }

    public void setPermission(User user, PermissionEnum permissionEnum, boolean value) {
        if (value) {
            if (hasPermission(user, permissionEnum)) {
                // Already has permission
                return;
            }
            // Add permission
            new Permission(user, permissionEnum, this).save();
        } else {
            if (hasPermission(user, permissionEnum)) {
                // Remove permission
                removePermission(user, permissionEnum);
            }
            // Already doesn't have permission
        }

    }

    @SuppressWarnings("unused")
    public void removePermission(User user, PermissionEnum permissionEnum) {
        if (!hasPermission(user, permissionEnum)) {
            return;
        }
        Permission permission = CommonMain.instance.getDatabaseManager().getPermissions(user, this).stream().filter(p -> p.permission.equals(permissionEnum)).findAny().orElse(null);

        if (permission == null) {
            return;
        }

        permission.delete();
    }

    /**
     * @return Unique identifier for the server / node / global in the format {@link PermissionEnum.PermissionType}_{@link #id}
     */
    public String getIdentifier() {
        return getType() + "_" + id;
    }

    public abstract PermissionEnum.PermissionType getType();

    @SuppressWarnings("unused")
    public List<Permission> getPermissions() {
        return CommonMain.instance.getDatabaseManager().getPermissions(this);
    }

    @Override
    public String toString() {
        return getIdentifier();
    }
}
