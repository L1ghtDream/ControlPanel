package dev.lightdream.node.manager;

import dev.lightdream.common.annotations.RedisEventHandler;
import dev.lightdream.common.database.Node;
import dev.lightdream.common.dto.redis.event.impl.ExecuteCommandEvent;
import dev.lightdream.node.Main;

@SuppressWarnings("unused")
public class RedisEventListener extends dev.lightdream.common.manager.RedisEventListener {

    public RedisEventListener(Main main) {
        super(main);
    }

    @RedisEventHandler
    public void onCommandExecute(ExecuteCommandEvent event) {
        event.respond(Node.executeCommandLocal(event.command));
    }


}
