package dev.lightdream.common.database;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.permission.PermissionContainer;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.dto.redis.RedisResponse;
import dev.lightdream.common.dto.redis.event.impl.ExecuteCommandEvent;
import dev.lightdream.common.manager.SSHManager;
import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.logger.Debugger;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@DatabaseTable(table = "nodes")
@NoArgsConstructor
public class Node extends PermissionContainer {

    //Settings
    @DatabaseField(columnName = "node_id", unique = true)
    public String nodeID;
    @DatabaseField(columnName = "name")
    public String name;
    //Credentials
    @DatabaseField(columnName = "ip")
    public String ip;
    @DatabaseField(columnName = "username")
    public String username;
    @DatabaseField(columnName = "ssh_port")
    public int sshPort;

    public Node(String nodeID, String name, String ip, String username, int sshPort) {
        this.nodeID = nodeID;
        this.name = name;
        this.ip = ip;
        this.username = username;
        this.sshPort = sshPort;
    }

    /**
     * Executes a command directly on the current machine (node)
     *
     * @param command The command to execute
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

    @Override
    public PermissionEnum.PermissionType getType() {
        return PermissionEnum.PermissionType.NODE;
    }

    public SSHManager.NodeSSH getSSH() {
        return CommonMain.instance.getSSHManager().getSSH(this);
    }

    public String executeCommand(String command) {
        return executeCommand(command, false);
    }

    /**
     * Executes a command on the node via SSH or redis
     *
     * @param command The command to execute
     * @param local   Whether to execute the command via SSH or redis
     * @return The output of the command
     */
    @SneakyThrows
    public String executeCommand(String command, boolean local) {
        if (local) {
            RedisResponse response = _executeCommandLocal(command);
            if (response == null || response.response == null) {
                return "";
            }
            return response.response.toString();
        }
        return executeCommandSSH(command);
    }

    @SuppressWarnings("BusyWait")
    @SneakyThrows
    private RedisResponse _executeCommandLocal(String command) {
        RedisResponse response = CommonMain.instance.redisManager.send(new ExecuteCommandEvent(command, this));

        while (!response.isFinished()) {
            Thread.sleep(100);
        }

        return response;
    }

    /**
     * Executes a command on the node via SSH
     *
     * @param command The command to execute
     * @return The output of the command
     */
    @SneakyThrows
    private String executeCommandSSH(String command) {
        SSHManager.NodeSSH ssh = getSSH();
        SSHManager.SSHSession session = ssh.createNew();
        session.setCommand(command);

        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        session.setOutputStream(responseStream);

        while (session.isConnected()) {
            //noinspection BusyWait
            Thread.sleep(100);
        }

        return responseStream.toString();
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
        return executeCommand("screen -S " + server.serverID + " -X stuff '" + command + "^M'", true);
    }

    public List<Server> getServers() {
        return CommonMain.instance.getServers().stream().filter(server -> server.node.nodeID.equals(this.nodeID)).collect(Collectors.toList());
    }

    @SuppressWarnings("unused")
    public void installSFTPModule() {
        executeCommand(CommonMain.instance.getConfig().SFTP_MODULE_INSTALL_CMD.parse("path", CommonMain.instance.getConfig().sftpModuleInstallPath).parse("url", CommonMain.instance.getConfig().sftpModuleDownloadURL.parse("version", CommonMain.instance.getVersion()).parse()).parse());
    }

    @SuppressWarnings("unused")
    public void install() {
        executeCommand(CommonMain.instance.getConfig().EXECUTE_SCRIPT_CMD.parse("url", CommonMain.instance.getConfig().nodeInstallScriptURL).parse());
    }

}
