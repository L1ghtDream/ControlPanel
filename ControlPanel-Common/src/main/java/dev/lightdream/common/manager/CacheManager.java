package dev.lightdream.common.manager;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.Server;
import dev.lightdream.common.dto.Cache;
import dev.lightdream.logger.Logger;
import lombok.AllArgsConstructor;

import java.util.HashMap;

public class CacheManager {

    public Cache<ServersCache<Double>> memoryUsageCache;
    public Cache<ServersCache<Double>> memoryAllocationCache;
    public Cache<ServersCache<Double>> cpuUsageCache;
    public Cache<ServersCache<Double>> storageUsageCache;
    public Cache<ServersCache<Boolean>> onlineStatusCache;

    public CacheManager() {
        Logger.good("Starting caching... @ " + System.currentTimeMillis());

        memoryUsageCache = new Cache<>(cache -> {
            HashMap<Integer, Double> output = new HashMap<>();

            CommonMain.instance.getServers().forEach(server -> {
                //System.out.println("Caching memory usage for server " + server.serverID);
                //output.put(server.id, server.getMemoryUsageReal());
                output.put(server.id, 0.0);
            });

            cache.update(new ServersCache<>(output));
        }, 10 * 1000L); // 10s

        memoryAllocationCache = new Cache<>(cache -> {
            HashMap<Integer, Double> output = new HashMap<>();

            CommonMain.instance.getServers().forEach(server -> {
                //System.out.println("Caching memory allocation for server " + server.serverID);
                //output.put(server.id, server.getMemoryAllocationReal());
                output.put(server.id, 0.0);
            });

            cache.update(new ServersCache<>(output));
        }, 10 * 1000L); // 10s

        cpuUsageCache = new Cache<>(cache -> {
            HashMap<Integer, Double> output = new HashMap<>();

            CommonMain.instance.getServers().forEach(server -> {
                //System.out.println("Caching cpu usage for server " + server.serverID);
                //output.put(server.id, server.getCPUUsageReal());
                output.put(server.id, 0.0);
            });

            cache.update(new ServersCache<>(output));
        }, 20 * 1000L); // 20s

        storageUsageCache = new Cache<>(cache -> {
            HashMap<Integer, Double> output = new HashMap<>();

            CommonMain.instance.getServers().forEach(server -> {
                //System.out.println("Caching storage usage for server " + server.serverID);
                //output.put(server.id, server.getStorageUsageReal());
                output.put(server.id, 0.0);
            });

            cache.update(new ServersCache<>(output));
        }, 60 * 60 * 1000L); // 1h

        onlineStatusCache = new Cache<>(cache -> {
            HashMap<Integer, Boolean> output = new HashMap<>();

            CommonMain.instance.getServers().forEach(server -> {
                //System.out.println("Caching online status for server " + server.serverID);
                //output.put(server.id, server.isOnlineReal());
                output.put(server.id, false);
            });

            cache.update(new ServersCache<>(output));
        }, 20 * 1000L); // 20s

        Logger.good("Caching enabled! @ " + System.currentTimeMillis());
    }

    @AllArgsConstructor
    public static class ServersCache<T> {
        public HashMap<Integer, T> servers;

        public T get(Server server) {
            return servers.get(server.id);
        }
    }

}
