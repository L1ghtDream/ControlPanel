package dev.lightdream.common.database;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.permission.PermissionContainer;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

@DatabaseTable(table = "servers")
@NoArgsConstructor
public class Server extends PermissionContainer {

    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    //Settings
    @DatabaseField(columnName = "server_id", unique = true)
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
     * @return The server's memory usage in kb (cached)
     */
    public Double getMemoryUsage() {
        return CommonMain.instance.cacheManager.memoryUsageCache.get().get(this);
    }

    /**
     * @return The server's memory usage in kb (real)
     */
    public Double getMemoryUsageReal() {
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
     * @return The server's memory usage in formatted (cached)
     */
    @SuppressWarnings("unused")
    public String getMemoryUsageFormatted() {
        return formatMemory(getMemoryUsage());
    }

    /**
     * @return Formats amount of memory initially in kb into KB, MB, GB, TB according to the size
     */
    @NotNull
    private String formatMemory(Double usage) {
        if (usage < 1024) {
            return decimalFormat.format(usage) + "KB";
        }
        if (usage < 1024 * 1024) {
            return decimalFormat.format(usage / 1024) + "MB";
        }
        if (usage < 1024 * 1024 * 1024) {
            return decimalFormat.format(usage / 1024 / 1024) + "GB";
        }
        return decimalFormat.format(usage / 1024 / 1024 / 1024) + "TB";
    }

    /**
     * @return The server's memory allocation in kb (cached)
     */
    public Double getMemoryAllocation() {
        return CommonMain.instance.cacheManager.memoryAllocationCache.get().get(this);
    }

    /**
     * @return The server's memory allocation in kb (real)
     */
    public Double getMemoryAllocationReal() {
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
     * @return The server's memory allocation formatted (cached)
     */
    @SuppressWarnings("unused")
    public String getMemoryAllocationFormatted() {
        return formatMemory(getMemoryAllocation());
    }

    /**
     * @return The server's CPU usage in percentages of a core (cached)
     */
    @SuppressWarnings("unused")
    public Double getCPUUsage() {
        return CommonMain.instance.cacheManager.cpuUsageCache.get().get(this);
    }

    /**
     * @return The server's CPU usage in percentages of a core (real)
     */
    public Double getCPUUsageReal() {
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
    public Double getStorageUsage() {
        return CommonMain.instance.cacheManager.storageUsageCache.get().get(this);
    }

    /**
     * @return The server's storage usage in kb (cached)
     */
    public Double getStorageUsageReal() {
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
     * @return The server's storage usage formatted (cached)
     */
    @SuppressWarnings("unused")
    public String getStorageUsageFormatted() {
        return formatMemory(getStorageUsage());
    }

    /**
     * @return Weather the server is running or not (cached)
     */
    @SuppressWarnings("unused")
    public boolean isOnline() {
        return CommonMain.instance.cacheManager.onlineStatusCache.get().get(this);
    }

    /**
     * @return Weather the server is running or not
     */
    public boolean isOnlineReal() {
        return getPID() != null;
    }
}
