package dev.lightdream.controlpanel.manager;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.Node;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;

public class SSHManager {

    public static SSHSession createSSHSession(Node node) {
        return new SSHSession(node);
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

}