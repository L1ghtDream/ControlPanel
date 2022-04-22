package dev.lightdream.common.dto.cache;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class CacheRegistry {

    public ServersCache<Double> memoryUsageCache = new ServersCache<>();
    public ServersCache<Double> memoryAllocationCache = new ServersCache<>();
    public ServersCache<Double> cpuUsageCache = new ServersCache<>();
    public ServersCache<Double> storageUsageCache = new ServersCache<>();
    public ServersCache<Boolean> onlineStatusCache = new ServersCache<>();

    public void updateRegistry(CacheRegistry registry) {
        memoryUsageCache.set(registry.memoryUsageCache);
        memoryAllocationCache.set(registry.memoryAllocationCache);
        cpuUsageCache.set(registry.cpuUsageCache);
        storageUsageCache.set(registry.storageUsageCache);
        onlineStatusCache.set(registry.onlineStatusCache);
    }

}
