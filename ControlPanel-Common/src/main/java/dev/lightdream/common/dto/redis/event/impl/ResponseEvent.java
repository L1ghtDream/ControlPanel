package dev.lightdream.common.dto.redis.event.impl;

import dev.lightdream.common.dto.redis.event.RedisEvent;
import dev.lightdream.common.utils.Utils;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResponseEvent extends RedisEvent {

    public String response;

    public ResponseEvent(RedisEvent command, Object response) {
        super(command.originator);
        this.id = command.id;
        this.response = Utils.toJson(response);
    }

}
