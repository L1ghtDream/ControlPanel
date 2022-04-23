package dev.lightdream.common.dto.redis.event.impl;

import dev.lightdream.common.annotations.RedisEventEnabler;
import dev.lightdream.common.database.Node;
import dev.lightdream.common.dto.redis.event.RedisEvent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@RedisEventEnabler
public class ExecuteCommandEvent extends RedisEvent {

    public String command;

    public ExecuteCommandEvent(String command, Node target) {
        super(target.nodeID);
        this.command = command;
    }

}
