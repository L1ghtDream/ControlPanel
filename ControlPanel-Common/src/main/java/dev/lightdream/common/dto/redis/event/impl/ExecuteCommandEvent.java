package dev.lightdream.common.dto.redis.event.impl;

import dev.lightdream.common.database.Node;
import dev.lightdream.common.dto.redis.event.RedisEvent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ExecuteCommandEvent extends RedisEvent {

    public String command;

    public ExecuteCommandEvent(String command, Node target) {
        super(target.getID());
        this.command = command;
    }

}
