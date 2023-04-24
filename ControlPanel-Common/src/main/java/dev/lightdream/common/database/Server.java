package dev.lightdream.common.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.ServerStats;
import dev.lightdream.common.dto.permission.PermissionContainer;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.logger.Debugger;
import dev.lightdream.messagebuilder.MessageBuilder;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "servers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id"})
        }
)
@NoArgsConstructor
public class Server extends PermissionContainer {

    @Id
    @Column(name = "id", nullable = false, unique = true, length = 11)
    public String id;
    // Settings
    @Column(name = "name")
    public String name;
    // Location
    @Column(name = "path")
    public String path;
    @ManyToOne
    @JoinColumn(name = "node")
    public Node node;
    @Column(name = "port")
    public Integer port;

    // Java Settings
    @Column(name = "java")
    public String java;
    @Column(name = "ram")
    public String ram;
    @Column(name = "server_jar")
    public String serverJar;
    @Column(name = "args")
    public String args;
    @Column(name = "start_if_offline")
    public Boolean startIfOffline;

    // Persistent vars
    /**
     * The server has been manually stopped and therefore should not be restarted automatically.
     */
    @Column(name = "stopped")
    public boolean stopped;

    /**
     * @param id        The server's id
     * @param name      The server's name
     * @param path      The server's path on the machine
     * @param node      The server's node (machine)
     * @param port      The server's port
     * @param java      The server's java version (JDK_8, JDK_11, JDK_16, JDK_17)
     * @param ram       The server's max ram
     * @param serverJar The server's jar file (server.jar)
     * @param args      Additional java arguments
     */
    public Server(String id, String name, String path, Node node, Integer port, String java, String ram,
                  String serverJar, String args, boolean startIfOffline) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.node = node;
        this.port = port;
        this.java = java;
        this.ram = ram;
        this.serverJar = serverJar;
        this.args = args;
        this.startIfOffline = startIfOffline;
        this.node.executeCommand("mkdir -p " + path);
        createStartScript();
        CommonMain.instance.registerServerLog(this);
    }

    @JsonIgnore
    public static Server getServer(String serverID) {
        return Utils.getServer(serverID);
    }

    @JsonIgnore
    public static List<Server> getServers() {
        return CommonMain.instance.databaseManager.getServers();
    }

    private void createStartScript() {
        this.node.executeCommand(CommonMain.instance.getConfig().CREATE_START_SCRIPT
                .parse("id", id)
                .parse("java", CommonMain.instance.getConfig().getJava(java))
                .parse("ram", ram)
                .parse("server_jar", serverJar)
                .parse("path", path)
                .parse("args", args)
                .parse()
        );
    }

    public void start() {
        createStartScript();
        this.node.executeCommand(CommonMain.instance.getConfig().SERVER_START_CMD
                .parse("path", path)
                .parse()
        );
        this.stopped = false;
    }

    @Override
    @JsonIgnore
    public PermissionEnum.Type getType() {
        return PermissionEnum.Type.SERVER;
    }

    @SneakyThrows
    public void sendCommand(String command) {
        node.sendCommandToServer(command, this);
    }

    @JsonIgnore
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
    @JsonIgnore
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
    @JsonIgnore
    @SuppressWarnings("unused")
    public Double getMemoryAllocation() {
        Integer pid = getPID();

        if (pid == null) {
            return 0.0;
        }

        String output = node.executeCommand(
                CommonMain.instance.getConfig().MEMORY_ALLOCATED_CMD
                        //.parse("pid", pid.toString())
                        .parse("path", path)
                        .parse()
        ).trim().replace("-Xmx", "");
        return Utils.memoryFromString(output);
    }

    /**
     * @return The server's CPU usage in percentages of a core (real)
     */
    @JsonIgnore
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
    @JsonIgnore
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
    public boolean isOnline() {
        //return getPID() != null;
        return screenExists();
    }

    @JsonIgnore
    public String getServerStatsCommand(Integer pid) {
        return new MessageBuilder(
                CommonMain.instance.getConfig().MEMORY_USAGE_CMD + " && " +
                        CommonMain.instance.getConfig().MEMORY_ALLOCATED_CMD + " && " +
                        CommonMain.instance.getConfig().CPU_USAGE_CMD + " && " +
                        CommonMain.instance.getConfig().STORAGE_USAGE_CMD + " && ")
                .parse("path", path)
                .parse("port", port)
                .parse("pid", pid)
                .parse();
    }

    @JsonIgnore
    public ServerStats getStats() {
        Integer pid = getPID();

        if (pid == null) {
            return new ServerStats(
                    this,
                    0.0,
                    0.0,
                    0.0,
                    getStorageUsage(),
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
                this,                              // Server ID
                Utils.doubleOrNegative(stats[0].trim()), // memory usage
                Utils.memoryFromString(stats[1].trim().replace("-Xmx", "")), // memory allocation
                Utils.doubleOrNegative(stats[2].trim()), // cpu usage
                Utils.doubleOrNegative(stats[3].trim()), // storage usage
                true                                     // online status
        );
    }

    @JsonIgnore
    @SuppressWarnings("unused")
    public String getSFTPUrl(int userID) {
        User user = User.getUser(userID);

        return CommonMain.instance.getConfig().sftpURL
                .parse("username", user.username + "_" + this.getID())
                .parse("host", node.ip)
                .parse("port", node.sshPort)
                .parse();
    }

    @JsonIgnore
    @SuppressWarnings("unused")
    public String getSFTPUrl() {
        return CommonMain.instance.getConfig().sftpURL
                .parse("username", "%username%_" + this.getID())
                .parse("host", node.ip)
                .parse("port", node.sftpPort)
                .parse();
    }

    @JsonIgnore
    public List<User> getUsers() {
        List<Permission> permissions = CommonMain.instance.databaseManager.getPermissions(this);
        List<User> users = new ArrayList<>();

        permissions.forEach(permission -> {

            User user = User.getUser(permission.user_id);
            if (users.contains(user)) {
                return;
            }
            users.add(user);
        });

        return users;
    }

    @Override
    public String toString() {
        return Utils.toJson(this);
    }

    public void kill() {
        //String response = node.executeCommand("screen -ls " + server.id);
        node.executeCommand(CommonMain.instance.getConfig().KILL_CMD
                .parse("port", this.port)
                .parse()
        );
        stopped = true;
    }

    public boolean screenExists() {
        String response = node.executeCommand(CommonMain.instance.getConfig()
                .SCREEN_LIST_CMD
                .parse("id", this.id)
                .parse());
        return response != null && !response.contains("No Sockets found") && !response.equals("");
    }

    public void autoRestartCall() {

        Debugger.log("Attempting Auto restarting server " + this.id);

        if (isOnline() || stopped) {
            return;
        }

        Debugger.log("------------------");
        Debugger.log("Server " + this.id + " is offline, restarting...");
        Debugger.log("------------------");
        start();
    }

    public void stop() {
        this.stopped = true;
        save();
        this.sendCommand("stop");
    }

    @Override
    public String getID() {
        return id;
    }
}
