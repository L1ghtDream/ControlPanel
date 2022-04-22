package dev.lightdream.common.dto.redis.command.impl;

import dev.lightdream.common.dto.redis.command.RedisCommand;
import dev.lightdream.common.dto.redis.event.RedisEvent;
import dev.lightdream.common.dto.redis.event.impl.ExecuteEvent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ExecuteCommand extends RedisCommand {

    public String command;

    public ExecuteCommand(String command) {
        this.command = command;
    }

    @Override
    public RedisEvent getEvent() {
        return new ExecuteEvent(this);
    }

}
