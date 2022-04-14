package dev.lightdream.controlpanel.database;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import dev.lightdream.controlpanel.Main;
import dev.lightdream.controlpanel.dto.User;
import dev.lightdream.controlpanel.dto.permission.Permission;
import dev.lightdream.controlpanel.dto.permission.PermissionType;
import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.DatabaseEntry;
import dev.lightdream.lambda.LambdaExecutor;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@DatabaseTable(table = "nodes")
public class Node extends DatabaseEntry {

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

    //Node Specific
    @DatabaseField(columnName = "permissions")
    public List<Permission> permissions;

    //Connections
    public Session session;
    public ChannelExec channel;
    public Session logSession;
    public ChannelExec logChannel;

    @SuppressWarnings("unused")
    public Node() {
        super(Main.instance);
    }

    public Node(String nodeID, String name, String nodeIP, String publicIP, String password, String username, int sshPort) {
        super(Main.instance);
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

    // Perms

    public List<Permission> gerUserPermissions(User user) {
        return permissions.stream().filter(permission -> permission.user.equals(user)).collect(Collectors.toList());
    }

    public boolean hasPermission(User user, PermissionType permissionType) {
        return gerUserPermissions(user).stream().anyMatch(permission -> permission.permission.equals(permissionType));
    }

    @SuppressWarnings("unused")
    public void addPermission(User user, PermissionType permissionType) {
        if (hasPermission(user, permissionType)) {
            return;
        }
        permissions.add(new Permission(user, permissionType));
        save();
    }

    @SuppressWarnings("unused")
    public void removePermission(User user, PermissionType permissionType) {
        if (!hasPermission(user, permissionType)) {
            return;
        }
        permissions.removeIf(permission -> permission.user.equals(user) && permission.permission.equals(permissionType));
        save();
    }


}
