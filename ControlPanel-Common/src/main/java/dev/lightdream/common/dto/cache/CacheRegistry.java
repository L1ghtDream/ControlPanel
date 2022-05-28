package dev.lightdream.common.dto.cache;

import dev.lightdream.common.database.Server;
import dev.lightdream.common.dto.ServerStats;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class CacheRegistry {

    public ServersCache<Double> memoryUsageCache = new ServersCache<>(0.0);
    public ServersCache<Double> memoryAllocationCache = new ServersCache<>(0.0);
    public ServersCache<Double> cpuUsageCache = new ServersCache<>(0.0);
    public ServersCache<Double> storageUsageCache = new ServersCache<>(0.0);
    public ServersCache<Boolean> onlineStatusCache = new ServersCache<>(false);

    @SuppressWarnings("unused")
    public void updateRegistry(CacheRegistry registry) {
        memoryUsageCache.set(registry.memoryUsageCache);
        memoryAllocationCache.set(registry.memoryAllocationCache);
        cpuUsageCache.set(registry.cpuUsageCache);
        storageUsageCache.set(registry.storageUsageCache);
        onlineStatusCache.set(registry.onlineStatusCache);
    }

    public ServerStats getStats(Server server) {
        return new ServerStats(
                server,
                memoryUsageCache.get(server),
                memoryAllocationCache.get(server),
                cpuUsageCache.get(server),
                storageUsageCache.get(server),
                onlineStatusCache.get(server)
        );

    }


}
