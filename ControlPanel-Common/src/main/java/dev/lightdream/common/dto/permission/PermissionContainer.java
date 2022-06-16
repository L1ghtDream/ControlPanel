package dev.lightdream.common.dto.permission;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.GlobalPermissionContainer;
import dev.lightdream.common.database.Permission;
import dev.lightdream.common.database.User;
import dev.lightdream.databasemanager.dto.entry.impl.StringDatabaseEntry;
import dev.lightdream.logger.Debugger;

import java.util.List;

public abstract class PermissionContainer extends StringDatabaseEntry {

    public PermissionContainer() {
        super(CommonMain.instance);
    }

    /**
     * @param identifier Unique identifier for the server / node / global in the format
     *                   {@link PermissionEnum.Type}_{@link #id}
     * @return The server / node / global context of the identifier
     */
    public static PermissionContainer getByIdentifier(String identifier) {
        Debugger.log("[3.1]" +identifier);
        PermissionEnum.Type type = PermissionEnum.Type.valueOf(identifier.split("_")[0]);
        String id = identifier.split("_")[1];
        Debugger.log("[3.2]" + type);
        Debugger.log("[3.3]" + id);

        switch (type) {
            case SERVER:
                PermissionContainer server =  CommonMain.instance.getDatabaseManager().getServer(id);
                Debugger.log("[3.4]" + server);
                if(server==null){
                    return GlobalPermissionContainer.getInstance();
                }
                return server;
            case GLOBAL:
                PermissionContainer global = GlobalPermissionContainer.getInstance();
                Debugger.log("[3.5]" + global);
                return global;
        }

        return null;
    }

    public List<Permission> gerUserPermissions(User user) {
        return CommonMain.instance.getDatabaseManager().getPermissions(user, this);
    }

    public boolean hasPermission(User user, PermissionEnum permission) {
        Debugger.log("[1.1]"+user);
        Debugger.log("[1.2]"+permission);
        Debugger.log("[1.3]"+CommonMain.instance);
        Debugger.log("[1.4]"+CommonMain.instance.getDatabaseManager());
        Debugger.log("[1.5]"+CommonMain.instance.getDatabaseManager().getPermissions(user));
        if (permission.getType().equals(PermissionEnum.Type.SERVER) &&
                CommonMain.instance.getDatabaseManager().getPermissions(user).stream().anyMatch(p -> {
                    if (p == null) {
                        return false;
                    }
                    return p.permission.equals(PermissionEnum.GLOBAL_ADMIN);
                })) {
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
     * @return Unique identifier for the server / node / global in the format {@link PermissionEnum.Type}_{@link #id}
     */
    public String getIdentifier() {
        return getType() + "_" + id;
    }

    public abstract PermissionEnum.Type getType();

    @SuppressWarnings("unused")
    public List<Permission> getPermissions() {
        return CommonMain.instance.getDatabaseManager().getPermissions(this);
    }

    @Override
    public String toString() {
        return getIdentifier();
    }
}
