package dev.lightdream.common.database;

import dev.lightdream.common.dto.Log;
import dev.lightdream.common.manager.PermissionHolder;
import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import lombok.SneakyThrows;

@DatabaseTable(table = "servers")
public class Server extends PermissionHolder {

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

    //Data
    public Log log;

    public Server(String serverID, String name, String path, Node node) {
        this.serverID = serverID;
        this.name = name;
        this.path = path;
        this.node = node;
        this.log = new Log();
    }

    @SuppressWarnings("unused")
    public Server() {
    }

    @SneakyThrows
    public void sendCommand(String command) {
        node.sendCommandToServer(command, this);
    }
}
