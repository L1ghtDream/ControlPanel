package dev.lightdream.common.dto.redis.event.impl;

import dev.lightdream.common.dto.redis.event.RedisEvent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResponseEvent extends RedisEvent {

    public Object response;

    public ResponseEvent(RedisEvent command, Object response) {
        super(command.originator);
        this.id = command.id;
        this.response = response;
    }

}
