package dev.lightdream.common.dto.permission;

import java.util.ArrayList;
import java.util.List;

public enum PermissionEnum {

    //TODO Add permissions for creating servers

    // Global perms
    GLOBAL_ADMIN("Administrator", "Gives permission to all server functions"),
    GLOBAL_MANAGE_USERS("Manage users", "Create / Delete / Change password / Disable OTP for users"),
    GLOBAL_MANAGE_NODES("Manage Nodes", "Create / Delete / Manage nodes"),
    GLOBAL_VIEW("View Dashboard", "Create / Delete / Manage nodes"),
    GLOBAL_CREATE_SERVER("Create Server", "Create new servers"),

    // Server Perms
    SERVER_VIEW("View Server", "View server in server list"),
    SERVER_CONSOLE("View server console", "Read, Send commands to the server"),
    SERVER_CONTROL("Control Server", "Start, Stop, Restart, Kill"),
    SERVER_MANAGE("Manager Server", "Delete server and change server settings"),
    SERVER_FILE_MANAGER("File Manager", "Read, Write Files using SFTP"),
    SERVER_USER_MANAGER("User Manager", "Add, remover users, Change perms");

    public final String title;
    public final String description;

    PermissionEnum(String Title, String description) {
        this.title = Title;
        this.description = description;
    }

    @SuppressWarnings("unused")
    public static List<PermissionEnum> getOfType(PermissionType type) {
        List<PermissionEnum> output = new ArrayList<>();

        for (PermissionEnum value : PermissionEnum.values()) {
            if (value.getType().equals(type)) {
                output.add(value);
            }
        }

        return output;
    }

    public String getName() {
        return this.name();
    }

    public String getTitle() {
        return this.title;
    }

    @SuppressWarnings("unused")
    public String getDescription() {
        return this.description;
    }

    public PermissionType getType() {
        return PermissionType.valueOf(this.name().split("_")[0]);
    }

    public enum PermissionType {
        SERVER,
        GLOBAL
    }

}
