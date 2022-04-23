package dev.lightdream.common.database;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.ServerStats;
import dev.lightdream.common.dto.permission.PermissionContainer;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.messagebuilder.MessageBuilder;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@DatabaseTable(table = "servers")
@NoArgsConstructor
public class Server extends PermissionContainer {

    //Settings
    @DatabaseField(columnName = "name")
    public String name;
    //Location
    @DatabaseField(columnName = "path")
    public String path;
    @DatabaseField(columnName = "node")
    public Node node;
    @DatabaseField(columnName = "port")
    public Integer port;

    public Server(String id, String name, String path, Node node, Integer port) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.node = node;
        this.port = port;
    }

    public static Server getServer(String serverID) {
        return Utils.getServer(serverID);
    }

    @Override
    public PermissionEnum.PermissionType getType() {
        return PermissionEnum.PermissionType.SERVER;
    }

    @SneakyThrows
    public void sendCommand(String command) {
        node.sendCommandToServer(command, this);
    }

    @Nullable
    public Integer getPID() {
        String output = node.executeCommand(
                CommonMain.instance.getConfig().PID_GRAB_CMD
                        .parse("port", String.valueOf(port))
                        .parse()
        ).trim();

        if (output.equals("")) {
            return null;
        }

        return Integer.parseInt(output);
    }

    /**
     * @return The server's memory usage in kb (real)
     */
    @SuppressWarnings("unused")
    public Double getMemoryUsage() {
        Integer pid = getPID();

        if (pid == null) {
            return 0.0;
        }

        return Double.parseDouble(node.executeCommand(
                CommonMain.instance.getConfig().MEMORY_USAGE_CMD
                        .parse("pid", pid.toString())
                        .parse()
        ).trim());
    }

    /**
     * @return The server's memory allocation in kb (real)
     */
    @SuppressWarnings("unused")
    public Double getMemoryAllocation() {
        Integer pid = getPID();

        if (pid == null) {
            return 0.0;
        }

        return Double.parseDouble(node.executeCommand(
                CommonMain.instance.getConfig().MEMORY_ALLOCATED_CMD
                        .parse("pid", pid.toString())
                        .parse()
        ).trim());
    }

    /**
     * @return The server's CPU usage in percentages of a core (real)
     */
    @SuppressWarnings("unused")
    public Double getCPUUsage() {
        Integer pid = getPID();

        if (pid == null) {
            return 0.0;
        }


        return Double.parseDouble(node.executeCommand(
                CommonMain.instance.getConfig().CPU_USAGE_CMD
                        .parse("pid", pid.toString())
                        .parse()
        ).trim());
    }

    /**
     * @return The server's storage usage in kb (cached)
     */
    @SuppressWarnings("unused")
    public Double getStorageUsage() {
        String data = node.executeCommand(
                CommonMain.instance.getConfig().STORAGE_USAGE_CMD
                        .parse("path", path)
                        .parse()
        ).trim().replaceAll("^[\n\r]", "").replaceAll("[\n\r]$", "");

        if (data.equals("")) {
            return 0.0;
        }

        return Double.parseDouble(data);
    }

    /**
     * @return Weather the server is running or not
     */
    @SuppressWarnings("unused")
    public boolean isOnline() {
        return getPID() != null;
    }

    public String getServerStatsCommand(@NotNull Integer pid) {
        return new MessageBuilder(" && ",
                CommonMain.instance.getConfig().MEMORY_USAGE_CMD,
                CommonMain.instance.getConfig().MEMORY_ALLOCATED_CMD,
                CommonMain.instance.getConfig().CPU_USAGE_CMD,
                CommonMain.instance.getConfig().STORAGE_USAGE_CMD)
                .parse("path", path)
                .parse("port", port)
                .parse("pid", pid)
                .parse();
    }

    public ServerStats getStats() {
        Integer pid = getPID();

        if (pid == null) {
            return new ServerStats(
                    this,
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    false
            );
        }

        String command = getServerStatsCommand(pid);

        String output = this.node.executeCommand(command);

        if (output == null) {
            return new ServerStats();
        }

        String[] stats = output.split("\n");
        return new ServerStats(
                this,                     // Server ID
                Double.parseDouble(stats[0]), // memory usage
                Double.parseDouble(stats[1]), // memory allocation
                Double.parseDouble(stats[2]), // cpu usage
                Double.parseDouble(stats[3]), // storage usage
                true                          // online status
        );
    }
}
