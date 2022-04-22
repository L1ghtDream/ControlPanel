package dev.lightdream.node.manager;

import dev.lightdream.common.annotations.RedisEventHandler;
import dev.lightdream.common.database.Node;
import dev.lightdream.common.dto.redis.event.impl.ExecuteCommandEvent;
import dev.lightdream.logger.Debugger;

@SuppressWarnings("unused")
public class RedisEventListener extends dev.lightdream.common.manager.RedisEventListener {

    public RedisEventListener() {
        super();
    }

    @RedisEventHandler
    public void onCommandExecute(ExecuteCommandEvent event) {
        Debugger.log("Received execute event with command: " + event.command);
        event.respond(Node.executeCommandLocal(event.command));
    }


}
