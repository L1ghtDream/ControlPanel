package dev.lightdream.common.dto.redis.command.impl;

import dev.lightdream.common.dto.redis.command.RedisCommand;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RedisResponseCommand extends RedisCommand {

    public Object response;

    public RedisResponseCommand(RedisCommand command, Object response) {
        this.id = command.id;
        this.response = response;
    }


}
