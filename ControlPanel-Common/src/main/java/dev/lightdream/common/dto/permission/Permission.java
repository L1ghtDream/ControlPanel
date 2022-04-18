package dev.lightdream.common.dto.permission;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.User;
import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.DatabaseEntry;

@DatabaseTable(table = "permissions")
public class Permission extends DatabaseEntry {

    @DatabaseField(columnName = "user_id")
    public User user;
    @DatabaseField(columnName = "permission")
    public PermissionEnum permission;
    @DatabaseField(columnName = "target")
    public PermissionContainer target;

    @SuppressWarnings("unused")
    public Permission() {
        super(CommonMain.instance);
    }

    public Permission(User user, PermissionEnum permission, PermissionContainer target) {
        super(CommonMain.instance);
        this.user = user;
        this.permission = permission;
        this.target = target;
    }
}
