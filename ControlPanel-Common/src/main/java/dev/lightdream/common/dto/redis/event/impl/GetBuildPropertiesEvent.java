package dev.lightdream.common.dto.redis.event.impl;

import dev.lightdream.common.database.Node;
import dev.lightdream.common.dto.redis.event.RedisEvent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GetBuildPropertiesEvent extends RedisEvent {

    public GetBuildPropertiesEvent(Node target) {
        super(target.id);
    }

}
