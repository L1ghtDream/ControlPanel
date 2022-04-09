package dev.lightdream.controlpanel.utils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import dev.lightdream.controlpanel.Executor;
import dev.lightdream.controlpanel.Main;
import dev.lightdream.controlpanel.dto.Log;
import dev.lightdream.controlpanel.dto.Node;
import dev.lightdream.controlpanel.dto.Server;
import dev.lightdream.controlpanel.service.ConsoleService;
import dev.lightdream.lambda.LambdaExecutor;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class Utils {

    public static Server getServer(String id) {
        return Executor.servers.stream().filter(server -> server.id.equals(id)).findFirst().orElse(null);
    }

    public static Node getNode(String id) {
        return Executor.nodes.stream().filter(node -> node.id.equals(id)).findFirst().orElse(null);
    }
}
