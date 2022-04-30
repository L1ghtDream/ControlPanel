package dev.lightdream.node.manager;

import dev.lightdream.common.dto.ServerStats;
import dev.lightdream.common.dto.cache.CacheRegistry;
import dev.lightdream.common.dto.redis.event.impl.CacheUpdateEvent;
import dev.lightdream.logger.Logger;
import dev.lightdream.node.Main;
import dev.lightdream.runnable.Runnable;

public class CacheManager {


    public CacheManager(Main main) {
        Logger.good("Starting caching...");

        new Runnable(cache -> {
            CacheRegistry registry = new CacheRegistry();

            main.getNode().getServers().forEach(server -> {
                ServerStats stats = server.getStats();

                registry.memoryUsageCache.set(server, stats.memoryUsage);
                registry.memoryAllocationCache.set(server, stats.memoryAllocation);
                registry.cpuUsageCache.set(server, stats.cpuUsage);
                registry.storageUsageCache.set(server, stats.storageUsage);
                registry.onlineStatusCache.set(server, stats.isOnline);

            });

            new CacheUpdateEvent(registry).send();
        }, 20 * 1000L); //20 seconds

        Logger.good("Caching enabled!");
    }

}
