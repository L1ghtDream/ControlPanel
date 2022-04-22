package dev.lightdream.common.dto.redis.event.impl;

import dev.lightdream.common.annotations.RedisEventEnabler;
import dev.lightdream.common.dto.redis.event.RedisEvent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@RedisEventEnabler
public class ResponseEvent extends RedisEvent {

    public Object response;

    public ResponseEvent(RedisEvent command, Object response) {
        this.id = command.id;
        this.response = response;
    }

}
