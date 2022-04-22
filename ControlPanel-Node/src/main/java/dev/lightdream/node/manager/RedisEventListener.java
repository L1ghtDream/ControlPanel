package dev.lightdream.node.manager;

import dev.lightdream.common.database.Node;
import dev.lightdream.common.dto.redis.event.impl.ExecuteEvent;
import dev.lightdream.logger.Debugger;

public class RedisEventListener extends dev.lightdream.common.manager.RedisEventListener {

    public RedisEventListener() {
        super();

        ExecuteEvent.registerListener(event -> {
            Debugger.log("Received execute event with command: " + event.command);
            Node.executeCommandLocal(event.command);
            event.respond("Yes this works!");
        });
    }


}
