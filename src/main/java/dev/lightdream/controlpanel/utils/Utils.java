package dev.lightdream.controlpanel.utils;

import dev.lightdream.controlpanel.Executor;
import dev.lightdream.controlpanel.dto.Node;
import dev.lightdream.controlpanel.dto.Server;

public class Utils {

    public static Server getServer(String id) {
        return Executor.servers.stream().filter(server -> server.id.equals(id)).findFirst().orElse(null);
    }

    @SuppressWarnings("unused")
    public static Node getNode(String id) {
        return Executor.nodes.stream().filter(node -> node.id.equals(id)).findFirst().orElse(null);
    }
}
