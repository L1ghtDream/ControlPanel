package dev.lightdream.common.database;

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
    @DatabaseField(columnName = "ip")
    public String ip;
    @DatabaseField(columnName = "username")
    public String username;
    @DatabaseField(columnName = "ssh_port")
    public int sshPort;

    @SuppressWarnings("unused")
    public Node() {
    }

    public Node(String nodeID, String name, String ip, String username, int sshPort) {
        this.nodeID = nodeID;
        this.name = name;
        this.ip = ip;
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
            ssh.setCommand(command);

            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            ssh.setOutputStream(responseStream);

            while (ssh.isConnected()) {
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

    @SuppressWarnings("unused")
    public void install() {
        sendCommand(CommonMain.instance.getConfig().EXECUTE_SCRIPT_CMD
                .parse("url", CommonMain.instance.getConfig().nodeInstallScriptURL)
                .parse()
        );
    }


}
