package dev.lightdream.node.manager;

import dev.lightdream.common.database.Node;
import dev.lightdream.common.dto.redis.event.impl.ExecuteEvent;

public class RedisEventListener extends dev.lightdream.common.manager.RedisEventListener {

    public RedisEventListener() {
        super();

        ExecuteEvent.registerListener(event -> Node.executeCommandLocal(event.command));
    }


}
