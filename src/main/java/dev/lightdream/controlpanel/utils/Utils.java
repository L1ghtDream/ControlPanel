package dev.lightdream.controlpanel.utils;

import dev.lightdream.controlpanel.Executor;
import dev.lightdream.controlpanel.database.Node;
import dev.lightdream.controlpanel.database.Server;

public class Utils {

    public static Server getServer(String id) {
        return Executor.servers.stream().filter(server -> server.serverID.equals(id)).findFirst().orElse(null);
    }

    @SuppressWarnings("unused")
    public static Node getNode(String id) {
        return Executor.nodes.stream().filter(node -> node.nodeID.equals(id)).findFirst().orElse(null);
    }
}
