package dev.lightdream.common.dto.permission;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Permission {

    public User user;
    public PermissionType permission;

    public GsonPermission toGsonCompatible() {
        return new GsonPermission(this);
    }

    public static class GsonPermission {

        public int id;
        public String type;

        public GsonPermission(Permission permission) {
            this.id = permission.user.id;
            this.type = permission.permission.name();
        }

        public Permission toPermission() {
            return new Permission(CommonMain.instance.getDatabaseManager().getUser(id), PermissionType.valueOf(type));
        }

    }
}
