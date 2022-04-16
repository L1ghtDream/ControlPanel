package dev.lightdream.common.dto.permission;

import java.util.ArrayList;
import java.util.List;

public enum PermissionEnum {

    // Node Perms
    /* NOT IMPLEMENTED */ NODE_VIEW, // View node
    /* NOT IMPLEMENTED */ NODE_MANAGE, // Create, Delete, Edit nodes
    /* NOT IMPLEMENTED */ NODE_CREATE_SERVER, // Create server on node

    // Server Perms
    /* NOT IMPLEMENTED */ SERVER_VIEW, // View server
    /* IMPLEMENTED */ SERVER_CONSOLE, // Read, Send commands
    /* IMPLEMENTED */ SERVER_CONTROL, // Start, Stop, Restart, Kill
    /* NOT IMPLEMENTED */ SERVER_MANAGE, // Delete server
    /* NOT IMPLEMENTED */ SERVER_FILE_MANAGER, // Read, Write Files
    /* NOT IMPLEMENTED */ SERVER_USER_MANAGER; // Add, remover users, Change perms


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
        NODE,
        SERVER
    }

}
