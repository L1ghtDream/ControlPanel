package dev.lightdream.controlpanel.database;

import dev.lightdream.controlpanel.Main;
import dev.lightdream.controlpanel.dto.Log;
import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.DatabaseEntry;
import lombok.SneakyThrows;

import java.util.List;

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
}
