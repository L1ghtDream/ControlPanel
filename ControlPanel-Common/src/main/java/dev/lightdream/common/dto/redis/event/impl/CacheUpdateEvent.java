package dev.lightdream.common.dto.redis.event.impl;

import dev.lightdream.common.annotations.RedisEventEnabler;
import dev.lightdream.common.dto.cache.CacheRegistry;
import dev.lightdream.common.dto.redis.event.RedisEvent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@RedisEventEnabler
public class CacheUpdateEvent extends RedisEvent {

    public CacheRegistry registry;

    public CacheUpdateEvent(CacheRegistry registry) {
        super("master");
        this.registry = registry;
    }
}
