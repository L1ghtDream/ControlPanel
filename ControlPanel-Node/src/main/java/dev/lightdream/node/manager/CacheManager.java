package dev.lightdream.node.manager;

import dev.lightdream.common.database.Node;
import dev.lightdream.common.dto.ServerStats;
import dev.lightdream.common.dto.cache.CacheRegistry;
import dev.lightdream.common.dto.redis.event.impl.CacheUpdateEvent;
import dev.lightdream.lambda.ScheduleUtils;
import dev.lightdream.logger.Logger;
import dev.lightdream.node.Main;

public class CacheManager {


    public CacheManager(Main main) {
        Logger.good("Starting caching...");

        ScheduleUtils.runTaskTimerAsync(() -> {
            CacheRegistry registry = new CacheRegistry();

            Node node = main.getNode();

            if (node == null) {
                Logger.error("The current node is initialized as a panel node. Please check the config file and " +
                        "the panel admin page > nodes");
                return;
            }

            node.getServers().forEach(server -> {
                ServerStats stats = server.getStats();

                registry.memoryUsageCache.set(server, stats.memoryUsage);
                registry.memoryAllocationCache.set(server, stats.memoryAllocation);
                registry.cpuUsageCache.set(server, stats.cpuUsage);
                registry.storageUsageCache.set(server, stats.storageUsage);
                registry.onlineStatusCache.set(server, stats.isOnline);

            });

            new CacheUpdateEvent(registry).send();
        }, 0,20 * 1000L ); //20 seconds

        Logger.good("Caching enabled!");
    }

}
