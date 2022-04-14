package dev.lightdream.controlpanel.database;

import dev.lightdream.controlpanel.Main;
import dev.lightdream.controlpanel.dto.Log;
import dev.lightdream.controlpanel.dto.User;
import dev.lightdream.controlpanel.dto.permission.Permission;
import dev.lightdream.controlpanel.dto.permission.PermissionType;
import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.DatabaseEntry;
import lombok.SneakyThrows;

import java.util.List;
import java.util.stream.Collectors;

@DatabaseTable(table = "servers")
public class Server extends DatabaseEntry {

    //Settings
    @DatabaseField(columnName = "server_id")
    public String serverID;
    @DatabaseField(columnName = "name")
    public String name;

    //Location
    @DatabaseField(columnName = "path")
    public String path;
    @DatabaseField(columnName = "node")
    public Node node;

    //Server specific
    @DatabaseField(columnName = "port")
    public List<Integer> ports;
    @DatabaseField(columnName = "permissions")
    public List<Permission> permissions;

    //Data
    public Log log;

    public Server(String serverID, String name, String path, Node node, List<Integer> ports) {
        super(Main.instance);
        this.serverID = serverID;
        this.name = name;
        this.path = path;
        this.node = node;
        this.log = new Log();
        this.ports = ports;
    }

    @SuppressWarnings("unused")
    public Server() {
        super(Main.instance);
    }

    @SneakyThrows
    public void sendCommand(String command) {
        node.sendCommandToServer(command, this);
    }

    // Perms

    public List<Permission> gerUserPermissions(User user) {
        return permissions.stream().filter(permission -> permission.user.equals(user)).collect(Collectors.toList());
    }

    public boolean hasPermission(User user, PermissionType permissionType) {
        return gerUserPermissions(user).stream().anyMatch(permission -> permission.permission.equals(permissionType));
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
