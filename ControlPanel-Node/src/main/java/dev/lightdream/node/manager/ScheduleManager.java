package dev.lightdream.node.manager;

import dev.lightdream.common.database.Node;
import dev.lightdream.common.database.Server;
import dev.lightdream.lambda.ScheduleUtils;
import dev.lightdream.node.Main;

public class ScheduleManager {

    public ScheduleManager() {
        registerAutoRestartSchedule();
    }

    public void registerAutoRestartSchedule() {
        ScheduleUtils.runTaskTimer(() ->
                        Node.getNode(Main.instance.config.nodeID).getServers().forEach(Server::autoRestartCall),
                0, 10 * 1000);
    }

}
