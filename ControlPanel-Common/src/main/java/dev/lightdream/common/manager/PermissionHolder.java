package dev.lightdream.common.manager;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.User;
import dev.lightdream.common.dto.permission.Permission;
import dev.lightdream.common.dto.permission.PermissionType;
import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.dto.DatabaseEntry;
import dev.lightdream.logger.Debugger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PermissionHolder extends DatabaseEntry {

    @DatabaseField(columnName = "permissions")
    public List<Permission> permissions = new ArrayList<>();

    public PermissionHolder() {
        super(CommonMain.instance);
    }


    public List<Permission> gerUserPermissions(User user) {
        return permissions.stream().filter(permission -> permission.user.equals(user)).collect(Collectors.toList());
    }

    public boolean hasPermission(User user, PermissionType permission) {
        Debugger.log(gerUserPermissions(user));
        Debugger.log(permission);
        return gerUserPermissions(user).stream().anyMatch(p -> {
            Debugger.log("Searching: " + p + " -> " + p.permission.equals(permission));
            return p.permission.equals(permission);
        });
    }

    @SuppressWarnings("unused")
    public void addPermission(User user, PermissionType permissionType) {
        if (hasPermission(user, permissionType)) {
            return;
        }
        permissions.add(new Permission(user, permissionType));
        save();
    }

    @SuppressWarnings("unused")
    public void removePermission(User user, PermissionType permissionType) {
        if (!hasPermission(user, permissionType)) {
            return;
        }
        permissions.removeIf(permission -> permission.user.equals(user) && permission.permission.equals(permissionType));
        save();
    }

}
