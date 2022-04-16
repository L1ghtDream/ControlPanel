package dev.lightdream.common.manager;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;
import dev.lightdream.common.database.Node;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;

public class SSHManager {

    private final HashMap<Integer, NodeSSH> sshMap = new HashMap<>();

    public NodeSSH getSSH(Node node) {
        NodeSSH ssh = sshMap.get(node.id);
        if (ssh == null) {
            return sshMap.put(node.id, new NodeSSH());
        }
        return ssh;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    public static class NodeSSH {
        public Session session;
        public ChannelExec channel;
        public Session logSession;
        public ChannelExec logChannel;
    }

}
