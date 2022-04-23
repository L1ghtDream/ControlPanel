package dev.lightdream.controlpanel.manager;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.Node;
import dev.lightdream.logger.Debugger;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class SSHManager {

    private final static HashMap<String, NodeSSH> sshMap = new HashMap<>();

    public static NodeSSH getSSH(Node node) {
        NodeSSH ssh = sshMap.get(node.getID());
        if (ssh == null) {
            sshMap.put(node.getID(), new NodeSSH(node));
            return getSSH(node);
        }
        return ssh;
    }

    public static class SSHSession {
        public String nodeID;
        public String username;
        public String ip;
        public int port;

        private Session session;
        private ChannelExec channel;

        public SSHSession(Node node) {
            this.nodeID = node.getID();
            this.username = node.username;
            this.ip = node.ip;
            this.port = node.sshPort;
            auth();
        }

        public void setCommand(String command) {
            channel.setCommand(command);
        }

        @SneakyThrows
        public void setOutputStream(ByteArrayOutputStream outputStream) {
            setOutputStream(outputStream, true);
        }

        @SneakyThrows
        public void setOutputStream(ByteArrayOutputStream outputStream, boolean connect) {
            channel.setOutputStream(outputStream);
            if (connect) {
                channel.connect();
            }
        }


        public boolean isConnected() {
            return channel.isConnected();
        }

        @SneakyThrows
        public void auth() {
            if (session == null || !session.isConnected()) {
                JSch jsch = new JSch();
                Debugger.log("Getting ssh key from " + CommonMain.instance.getDataFolder().toString() + "/ssh_keys/" + nodeID);
                Debugger.log("Username: " + username);
                Debugger.log("IP: " + ip);
                Debugger.log("Port: " + port);

                jsch.addIdentity(CommonMain.instance.getDataFolder().toString() + "/ssh_keys/" + nodeID);

                session = jsch.getSession(username, ip, port);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();
            }

            if (channel == null || !channel.isConnected()) {
                channel = (ChannelExec) session.openChannel("exec");
            }
        }

    }

    public static class NodeSSH {
        public Node node;

        public NodeSSH(Node node) {
            this.node = node;
        }

        public SSHSession createNew() {
            return new SSHSession(node);
        }
    }

}