package dev.lightdream.common.dto.redis.command.impl;

import dev.lightdream.common.dto.redis.command.RedisCommand;
import dev.lightdream.common.dto.redis.event.PublicKeyReceive;
import dev.lightdream.common.dto.redis.event.RedisEvent;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PublicKeySend extends RedisCommand {

    public String nodeID;
    public String publicKey;

    @Override
    public RedisEvent getEvent() {
        return new PublicKeyReceive(this);
    }





}
