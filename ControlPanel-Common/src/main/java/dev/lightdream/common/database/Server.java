package dev.lightdream.common.database;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.dto.permission.PermissionTarget;
import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import lombok.SneakyThrows;

@DatabaseTable(table = "servers")
public class Server extends PermissionTarget {

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
    @DatabaseField(columnName = "port")
    public int port;

    public Server(String serverID, String name, String path, Node node, int port) {
        this.serverID = serverID;
        this.name = name;
        this.path = path;
        this.node = node;
        this.port = port;
    }

    @SuppressWarnings("unused")
    public Server() {
    }

    @Override
    public PermissionEnum.PermissionType getType() {
        return PermissionEnum.PermissionType.SERVER;
    }

    @SneakyThrows
    public void sendCommand(String command) {
        node.sendCommandToServer(command, this);
    }

    /**
     * @return The server's PID running on the port
     */
    public Integer getPID() {
        return Integer.parseInt(node.executeCommand(
                CommonMain.instance.getConfig().PID_GRAB_CMD
                        .parse("port", String.valueOf(port))
                        .parse()
        ));
    }

    /**
     * @return The server's memory usage in kb
     */
    @SuppressWarnings("unused")
    public int getMemoryUsage() {
        return Integer.parseInt(node.executeCommand(
                CommonMain.instance.getConfig().MEMORY_USAGE_CMD
                        .parse("pid", String.valueOf(getPID()))
                        .parse()
        ));
    }

    /**
     * @return The server's memory allocation in kb
     */
    @SuppressWarnings("unused")
    public int getMemoryAllocation() {
        return Integer.parseInt(node.executeCommand(
                CommonMain.instance.getConfig().MEMORY_ALLOCATED_CMD
                        .parse("pid", String.valueOf(getPID()))
                        .parse()
        ));
    }

    /**
     * @return The server's CPU usage in percentages of a core
     */
    @SuppressWarnings("unused")
    public double getCPUUsage() {
        return Double.parseDouble(node.executeCommand(
                CommonMain.instance.getConfig().CPU_USAGE_CMD
                        .parse("pid", String.valueOf(getPID()))
                        .parse()
        ));
    }
}
