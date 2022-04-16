package dev.lightdream.common.manager;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.Node;
import dev.lightdream.logger.Debugger;
import lombok.SneakyThrows;

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
        //public String password;
        public String ip;
        public int port;

        public Session session;
        public ChannelExec channel;
        public Session logSession;
        public ChannelExec logChannel;

        public NodeSSH(Node node) {
            this.nodeID = node.nodeID;
            this.username = node.username;
            this.ip = node.ip;
            this.port = node.sshPort;
            auth();
        }

        public String sendCommand() {
            auth();
            return "";
        }

        @SneakyThrows
        public void auth() {
            if (session != null && session.isConnected()) {
                return;
            }

            JSch jsch = new JSch();
            Debugger.log("Grabbing ssh key from " + CommonMain.instance.getDataFolder().toString() + "/ssh_keys/" + nodeID);
            jsch.addIdentity(CommonMain.instance.getDataFolder().toString() + "/ssh_keys/" + nodeID);

            session = jsch.getSession(username, ip, port);
            //logSession.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
        }

    }

}
