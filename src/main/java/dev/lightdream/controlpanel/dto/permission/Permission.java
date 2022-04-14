package dev.lightdream.controlpanel.dto.permission;

import dev.lightdream.controlpanel.Main;
import dev.lightdream.controlpanel.dto.User;
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

    @Override
    public String toString() {
        return "Permission{" +
                "user=" + user +
                ", permission=" + permission +
                '}';
    }

    public static class GsonPermission {

        public int id;
        public String type;

        public GsonPermission(Permission permission) {
            this.id = permission.user.id;
            this.type = permission.permission.name();
        }

        public Permission toPermission() {
            return new Permission(Main.instance.databaseManager.getUser(id), PermissionType.valueOf(type));
        }

    }
}
