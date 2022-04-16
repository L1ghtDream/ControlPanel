package dev.lightdream.common.dto.permission;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.User;
import dev.lightdream.databasemanager.dto.DatabaseEntry;

import java.util.List;

public abstract class PermissionTarget extends DatabaseEntry {


    public PermissionTarget() {
        super(CommonMain.instance);
    }

    public static PermissionTarget getByIdentifier(String identifier) {
        PermissionEnum.PermissionType type = PermissionEnum.PermissionType.valueOf(identifier.split("_")[0]);
        int id = Integer.parseInt(identifier.split("_")[1]);

        PermissionTarget target = null;

        switch (type) {
            case SERVER:
                target = CommonMain.instance.getDatabaseManager().getServer(id);
                break;
            case NODE:
                target = CommonMain.instance.getDatabaseManager().getNode(id);
                break;
        }

        return target;

    }

    public List<Permission> gerUserPermissions(User user) {
        return CommonMain.instance.getDatabaseManager().getPermissions(user, this);
    }

    public boolean hasPermission(User user, PermissionEnum permission) {
        return gerUserPermissions(user).stream().anyMatch(p -> p.permission.equals(permission));
    }

    @SuppressWarnings("unused")
    public void addPermission(User user, PermissionEnum permissionEnum) {
        if (hasPermission(user, permissionEnum)) {
            return;
        }
        new Permission(user, permissionEnum, this).save();
    }

    @SuppressWarnings("unused")
    public void removePermission(User user, PermissionEnum permissionEnum) {
        if (!hasPermission(user, permissionEnum)) {
            return;
        }
        Permission permission = CommonMain.instance.getDatabaseManager().getPermissions(user, this)
                .stream().filter(p ->
                        p.permission.equals(permissionEnum)
                ).findAny().orElse(null);

        if (permission == null) {
            return;
        }

        permission.delete();
    }

    public String getPermissionIdentifier() {
        return getType() + "_" + id;
    }

    public abstract PermissionEnum.PermissionType getType();

    public List<Permission> getPermissions() {
        return CommonMain.instance.getDatabaseManager().getPermissions(this);
    }

}
