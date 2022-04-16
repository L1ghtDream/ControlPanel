package dev.lightdream.common.database;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.dto.permission.PermissionTarget;
import dev.lightdream.common.manager.SSHManager;
import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.lambda.LambdaExecutor;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@DatabaseTable(table = "nodes")
public class Node extends PermissionTarget {

    //Settings
    @DatabaseField(columnName = "node_id", unique = true)
    public String nodeID;
    @DatabaseField(columnName = "name")
    public String name;
    //Credentials
    @DatabaseField(columnName = "node_ip")
    public String nodeIP;
    @DatabaseField(columnName = "public_ip")
    public String publicIP;
    @DatabaseField(columnName = "password")
    public String password;
    @DatabaseField(columnName = "username")
    public String username;
    @DatabaseField(columnName = "ssh_port")
    public int sshPort;

    @SuppressWarnings("unused")
    public Node() {
    }

    public Node(String nodeID, String name, String nodeIP, String publicIP, String password, String username, int sshPort) {
        this.nodeID = nodeID;
        this.name = name;
        this.nodeIP = nodeIP;
        this.publicIP = publicIP;
        this.password = password;
        this.username = username;
        this.sshPort = sshPort;
    }

    @Override
    public PermissionEnum.PermissionType getType() {
        return PermissionEnum.PermissionType.NODE;
    }

    public SSHManager.NodeSSH getSSH() {
        return CommonMain.instance.getSSHManager().getSSH(this);
    }

    public String sendCommand(String command) {
        return LambdaExecutor.LambdaCatch.ReturnLambdaCatch.executeCatch(() -> {
            SSHManager.NodeSSH ssh = getSSH();
            Session session = ssh.session;
            ChannelExec channel = ssh.channel;


            if (session == null || !session.isConnected()) {
                session = new JSch().getSession(this.username, this.nodeIP, 22);
                session.setPassword(this.password);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();
            }

            if (channel == null || !channel.isConnected()) {
                channel = (ChannelExec) session.openChannel("exec");
            }

            channel.setCommand(command);

            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            channel.setOutputStream(responseStream);
            channel.connect();

            while (channel.isConnected()) {
                //noinspection BusyWait
                Thread.sleep(100);
            }

            return responseStream.toString();
        });
    }

    public String executeCommand(String command) {
        return sendCommand(command);
    }

    @SuppressWarnings("UnusedReturnValue")
    public String sendCommandToServer(String command, Server server) {
        return sendCommand("screen -S " + server.serverID + " -X stuff '" + command + "\\n'");
    }

    public List<Server> getServers() {
        return CommonMain.instance.getServers().stream().filter(server -> server.node.nodeID.equals(this.nodeID)).collect(Collectors.toList());
    }

    @SuppressWarnings("unused")
    public void installSFTPModule() {
        sendCommand(CommonMain.instance.getConfig().SFTP_MODULE_INSTALL_CMD
                .parse("path", CommonMain.instance.getConfig().sftpModuleInstallPath)
                .parse("url", CommonMain.instance.getConfig().sftpModuleDownloadURL
                        .parse("version", CommonMain.instance.getVersion())
                        .parse())
                .parse()
        );
    }


}
