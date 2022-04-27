package dev.lightdream.common.dto.permission;

import java.util.ArrayList;
import java.util.List;

public enum PermissionEnum {

    // Global perms
    GLOBAL_ADMIN, // Gives permission to all server functions
    GLOBAL_MANAGE_USERS, // Create / Delete / Change password / Disable OTP for users
    GLOBAL_MANAGE_NODES, // Create / Delete / Manage nodes

    // Server Perms
    SERVER_VIEW, // View server
    SERVER_CONSOLE, // Read, Send commands
    SERVER_CONTROL, // Start, Stop, Restart, Kill
    SERVER_MANAGE, // Delete server
    SERVER_FILE_MANAGER, // Read, Write Files
    SERVER_USER_MANAGER; // Add, remover users, Change perms


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

    public PermissionType getType() {
        return PermissionType.valueOf(this.name().split("_")[0]);
    }

    public enum PermissionType {
        SERVER,
        GLOBAL
    }

}
