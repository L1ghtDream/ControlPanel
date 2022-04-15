package dev.lightdream.common.database;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import dev.lightdream.common.CommonMain;
import dev.lightdream.common.manager.PermissionHolder;
import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.lambda.LambdaExecutor;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@DatabaseTable(table = "nodes")
public class Node extends PermissionHolder {

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

    //Connections
    public Session session;
    public ChannelExec channel;
    public Session logSession;
    public ChannelExec logChannel;

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

    public String sendCommand(String command) {
        return LambdaExecutor.LambdaCatch.ReturnLambdaCatch.executeCatch(() -> {
            if (this.session == null || !this.session.isConnected()) {
                this.session = new JSch().getSession(this.username, this.nodeIP, 22);
                this.session.setPassword(this.password);
                this.session.setConfig("StrictHostKeyChecking", "no");
                this.session.connect();
            }

            if (this.channel == null || !this.channel.isConnected()) {
                this.channel = (ChannelExec) this.session.openChannel("exec");
            }

            this.channel.setCommand(command);

            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            this.channel.setOutputStream(responseStream);
            this.channel.connect();

            while (this.channel.isConnected()) {
                //noinspection BusyWait
                Thread.sleep(100);
            }

            return responseStream.toString();
        });
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
