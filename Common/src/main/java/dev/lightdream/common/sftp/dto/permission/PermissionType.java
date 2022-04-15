package dev.lightdream.common.sftp.dto.permission;

public enum PermissionType {

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
    /* NOT IMPLEMENTED */ SERVER_USER_MANAGER, // Add, remover users, Change perms


}
