package dev.lightdream.common.manager;

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

    private final HashMap<Integer, NodeSSH> sshMap = new HashMap<>();

    public NodeSSH getSSH(Node node) {
        NodeSSH ssh = sshMap.get(node.id);
        if (ssh == null) {
            sshMap.put(node.id, new NodeSSH(node));
            return getSSH(node);
        }
        return ssh;
    }


    public static class NodeSSH {
        public String nodeID;
        public String username;
        public String ip;
        public int port;

        private Session session;
        private ChannelExec channel;
        private Session logSession;
        private ChannelExec logChannel;

        public NodeSSH(Node node) {
            this.nodeID = node.nodeID;
            this.username = node.username;
            this.ip = node.ip;
            this.port = node.sshPort;
            auth();
            authLog();
        }

        public void setCommand(String command) {
            auth();
            channel.setCommand(command);
        }

        public void setCommandLog(String command) {
            authLog();
            logChannel.setCommand(command);
        }

        @SneakyThrows
        public void setOutputStream(ByteArrayOutputStream outputStream) {
            channel.setOutputStream(outputStream);
            channel.connect();
        }

        @SneakyThrows
        public void setOutputStreamLog(ByteArrayOutputStream outputStream) {
            logChannel.setOutputStream(outputStream);
            logChannel.connect();
        }

        public boolean isConnected() {
            return channel.isConnected();
        }

        public boolean isConnectedLog() {
            return logChannel.isConnected();
        }

        @SneakyThrows
        public void auth() {
            if (session == null || !session.isConnected()) {
                JSch jsch = new JSch();
                Debugger.log("Grabbing ssh key from " + CommonMain.instance.getDataFolder().toString() + "/ssh_keys/" + nodeID);
                jsch.addIdentity(CommonMain.instance.getDataFolder().toString() + "/ssh_keys/" + nodeID);

                session = jsch.getSession(username, ip, port);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();
            }

            if (channel == null || !channel.isConnected()) {
                channel = (ChannelExec) session.openChannel("exec");
            }
        }

        @SneakyThrows
        public void authLog() {
            if (logSession == null || !logSession.isConnected()) {
                JSch jsch = new JSch();
                jsch.addIdentity(CommonMain.instance.getDataFolder().toString() + "/ssh_keys/" + nodeID);

                logSession = jsch.getSession(username, ip, port);
                logSession.setConfig("StrictHostKeyChecking", "no");
                logSession.connect();
            }
            if (logChannel == null || !logChannel.isConnected()) {
                logChannel = (ChannelExec) logSession.openChannel("exec");
            }
        }

    }

}
