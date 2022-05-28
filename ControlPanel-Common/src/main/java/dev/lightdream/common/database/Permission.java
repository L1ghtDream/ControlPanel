package dev.lightdream.common.database;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.permission.PermissionContainer;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.entry.impl.IntegerDatabaseEntry;
import dev.lightdream.logger.Debugger;

@DatabaseTable(table = "permissions")
public class Permission extends IntegerDatabaseEntry {

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

    public String getTitle() {
        return permission.title;
    }

    public String getName() {
        return permission.name();
    }

    @SuppressWarnings("unused")
    public String getDescription() {
        return permission.description;
    }

    @Override
    public void save() {
        if (!permission.getType().equals(target.getType())) {
            Debugger.info("Permission type mismatch: " + permission.getType() + " != " + target.getType() + " for "
                    + permission + " @ " + target.getIdentifier());
            return;
        }
        super.save();
    }
}
