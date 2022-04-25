package dev.lightdream.common.database;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.cache.CacheRegistry;
import dev.lightdream.common.dto.redis.RedisResponse;
import dev.lightdream.common.dto.redis.event.impl.ExecuteCommandEvent;
import dev.lightdream.common.dto.redis.event.impl.PingEvent;
import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.entry.impl.StringDatabaseEntry;
import dev.lightdream.messagebuilder.MessageBuilder;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@DatabaseTable(table = "nodes")
public class Node extends StringDatabaseEntry {

    //Settings
    @DatabaseField(columnName = "name")
    public String name;
    //Credentials
    @DatabaseField(columnName = "ip")
    public String ip;
    @DatabaseField(columnName = "username")
    public String username;
    @DatabaseField(columnName = "ssh_port")
    public int sshPort;

    public Node() {
        super(CommonMain.instance);
    }

    public Node(String id, String name, String ip, String username, int sshPort) {
        super(CommonMain.instance);
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.username = username;
        this.sshPort = sshPort;
    }

    public static Node getNode(String id) {
        return CommonMain.instance.databaseManager.getNode(id);
    }

    /**
     * Executes a command directly on the current machine (node)
     *
     * @param command The command to execurete
     * @return The output of the command
     */
    @SuppressWarnings("UnusedReturnValue")
    @SneakyThrows
    public static String executeCommandLocal(String command) {
        Process process = new ProcessBuilder("bash", "-c", command)
                .redirectErrorStream(true)
                .start();

        StringBuilder output = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;

        while ((line = br.readLine()) != null) {
            output.append(line).append("\n");
        }

        if (0 != process.waitFor()) {
            return null;
        }

        return output.toString();
    }

    public static List<Node> getNodes() {
        return CommonMain.instance.getNodes();
    }

    /**
     * Executes a command on the node via redis
     *
     * @param command The command to execute
     * @return The output of the command
     */
    @SneakyThrows
    public String executeCommand(String command) {
        if (getID().equals(CommonMain.instance.getRedisID())) {
            return executeCommandLocal(command);
        }

        String response = _executeCommandLocal(command).getResponse(String.class);
        if (response == null) {
            return "";
        }
        return response;
    }

    @SuppressWarnings("BusyWait")
    @SneakyThrows
    private RedisResponse _executeCommandLocal(String command) {
        return new ExecuteCommandEvent(command, this).sendAndWait();
    }

    /**
     * Executes a command on the node via SSH
     *
     * @param command The command to execute
     * @param server  The server to execute the command on
     * @return The output of the command
     */
    @SuppressWarnings("UnusedReturnValue")
    public String sendCommandToServer(String command, Server server) {
        return executeCommand("screen -S " + server.id + " -X stuff '" + command + "^M'");
    }

    public List<Server> getServers() {
        return CommonMain.instance.getServers().stream().filter(server -> server.node.id.equals(this.id)).collect(Collectors.toList());
    }

    @SuppressWarnings("unused")
    public void installSFTPModule() {
        executeCommand(CommonMain.instance.getConfig().SFTP_MODULE_INSTALL_CMD.parse("path", CommonMain.instance.getConfig().sftpModuleInstallPath).parse("url", CommonMain.instance.getConfig().sftpModuleDownloadURL.parse("version", CommonMain.instance.getVersion()).parse()).parse());
    }

    @SuppressWarnings("unused")
    public void install() {
        executeCommand(CommonMain.instance.getConfig().EXECUTE_SCRIPT_CMD.parse("url", CommonMain.instance.getConfig().nodeInstallScriptURL).parse());
    }

    @Deprecated
    public CacheRegistry getServersStats() {

        List<String> commands = new ArrayList<>();
        CacheRegistry registry = new CacheRegistry();
        List<Server> servers = CommonMain.instance.getServers(this);

        servers.forEach(server -> {
            Integer pid = server.getPID();

            if (pid == null) {
                registry.memoryUsageCache.set(server, 0.0);
                registry.memoryAllocationCache.set(server, 0.0);
                registry.cpuUsageCache.set(server, 0.0);
                registry.storageUsageCache.set(server, 0.0);
                registry.onlineStatusCache.set(server, false);
                return;
            }

            commands.add(server.getServerStatsCommand(pid));
        });

        String command = new MessageBuilder(" && ", commands).parse();

        String output = executeCommand(command);
        String[] stats = output.split("\n");

        for (int i = 0; i < stats.length / 4; i++) {
            Server server = servers.get(i);
            registry.memoryUsageCache.set(server, Double.parseDouble(stats[i]));
            registry.memoryAllocationCache.set(server, Double.parseDouble(stats[i + 1]));
            registry.cpuUsageCache.set(server, Double.parseDouble(stats[i + 2]));
            registry.storageUsageCache.set(server, Double.parseDouble(stats[i + 3]));
            registry.onlineStatusCache.set(server, true);
        }

        return registry;
    }

    @SuppressWarnings("unused")
    public boolean isOnline() {
        return !new PingEvent(this).sendAndWait().hasTimeout();
    }
}
