package dev.lightdream.common.dto.redis.event.impl;

import dev.lightdream.common.annotations.RedisEventEnabler;
import dev.lightdream.common.dto.cache.CacheRegistry;
import dev.lightdream.common.dto.redis.event.RedisEvent;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@RedisEventEnabler
public class CacheUpdateEvent extends RedisEvent {

    public CacheRegistry registry;

}
