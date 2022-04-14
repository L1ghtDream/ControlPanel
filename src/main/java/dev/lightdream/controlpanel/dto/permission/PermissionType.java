package dev.lightdream.controlpanel.dto.permission;

public enum PermissionType {

    // Node Perms
    NODE_VIEW, // View node
    NODE_MANAGE, // Create, Delete, Edit nodes
    NODE_CREATE_SERVER, // Create server on node

    // Server Perms
    SERVER_VIEW, // View server
    SERVER_CONSOLE, // Read, Send commands
    SERVER_CONTROL, // Start, Stop, Restart, Kill
    SERVER_MANAGE, // Delete server
    SERVER_FILE_MANAGER, // Read, Write Files
    SERVER_USER_MANAGER, // Add, remover users, Change perms


}
