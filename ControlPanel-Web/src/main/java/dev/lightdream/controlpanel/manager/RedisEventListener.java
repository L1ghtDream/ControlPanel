package dev.lightdream.controlpanel.manager;

import dev.lightdream.common.annotations.RedisEventHandler;
import dev.lightdream.common.dto.redis.event.impl.CacheUpdateEvent;
import dev.lightdream.controlpanel.Main;

public class RedisEventListener extends dev.lightdream.common.manager.RedisEventListener {

    public RedisEventListener(Main main) {
        super(main);
    }

    @SuppressWarnings("unused")
    @RedisEventHandler
    public void onCacheRegistryReceive(CacheUpdateEvent event) {
        Main.instance.serversCache.updateRegistry(event.registry);
    }


}
