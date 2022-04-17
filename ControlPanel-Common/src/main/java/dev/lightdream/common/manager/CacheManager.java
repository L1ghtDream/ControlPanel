package dev.lightdream.common.manager;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.Server;
import dev.lightdream.common.dto.Cache;
import dev.lightdream.logger.Logger;
import lombok.AllArgsConstructor;

import java.util.HashMap;

public class CacheManager {

    public Cache<ServersCache> memoryUsageCache;
    public Cache<ServersCache> memoryAllocationCache;
    public Cache<ServersCache> cpuUsageCache;
    public Cache<ServersCache> storageUsageCache;

    public CacheManager() {
        Logger.good("Starting caching...");

        memoryUsageCache = new Cache<>(cache -> {
            HashMap<Integer, Double> output = new HashMap<>();

            CommonMain.instance.getServers().forEach(server ->
                    output.put(server.id, server.getMemoryUsageReal()));

            cache.update(new ServersCache(output));
        }, 10 * 1000L); // 10s

        memoryAllocationCache = new Cache<>(cache -> {
            HashMap<Integer, Double> output = new HashMap<>();

            CommonMain.instance.getServers().forEach(server ->
                    output.put(server.id, server.getMemoryAllocationReal()));

            cache.update(new ServersCache(output));
        }, 10 * 1000L); // 10s

        cpuUsageCache = new Cache<>(cache -> {
            HashMap<Integer, Double> output = new HashMap<>();

            CommonMain.instance.getServers().forEach(server ->
                    output.put(server.id, server.getCPUUsageReal()));

            cache.update(new ServersCache(output));
        }, 20 * 1000L); // 20s

        storageUsageCache = new Cache<>(cache -> {
            HashMap<Integer, Double> output = new HashMap<>();

            CommonMain.instance.getServers().forEach(server ->
                    output.put(server.id, server.getStorageUsageReal()));

            cache.update(new ServersCache(output));
        }, 60 * 60 * 1000L); // 1h

        Logger.good("Caching enabled!");
    }

    @AllArgsConstructor
    public static class ServersCache {
        public HashMap<Integer, Double> servers;

        public Double get(Server server) {
            return servers.get(server.id);
        }
    }


}
