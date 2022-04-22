package dev.lightdream.node.manager;

import dev.lightdream.common.dto.cache.Cache;
import dev.lightdream.logger.Debugger;
import dev.lightdream.logger.Logger;
import dev.lightdream.node.Main;

public class CacheManager {


    public CacheManager() {
        Logger.good("Starting caching...");

        new Cache<>(cache -> {
            Main.instance.getServers(Main.instance.getNode()).forEach(server -> {
                Debugger.log("Sending stats of server " + server.serverID + " to cache registry");
            });
        }, 20 * 1000L); //20 seconds

        /*
        memoryUsageCache = new Cache<>(cache -> {
            HashMap<Integer, Double> output = new HashMap<>();

            CommonMain.instance.getServers().forEach(server ->
                    output.put(server.id, server.getMemoryUsage()));

            cache.update(new ServersCache<>(output));
            cache.cancel();
        }, 10 * 1000L); // 10s

        memoryAllocationCache = new Cache<>(cache -> {
            HashMap<Integer, Double> output = new HashMap<>();

            CommonMain.instance.getServers().forEach(server ->
                    output.put(server.id, server.getMemoryAllocation()));

            cache.update(new ServersCache<>(output));
            cache.cancel();
        }, 10 * 1000L); // 10s

        cpuUsageCache = new Cache<>(cache -> {
            HashMap<Integer, Double> output = new HashMap<>();

            CommonMain.instance.getServers().forEach(server ->
                    output.put(server.id, server.getCPUUsage()));

            cache.update(new ServersCache<>(output));
            cache.cancel();
        }, 20 * 1000L); // 20s

        storageUsageCache = new Cache<>(cache -> {
            HashMap<Integer, Double> output = new HashMap<>();

            CommonMain.instance.getServers().forEach(server ->
                    output.put(server.id, server.getStorageUsage()));

            cache.update(new ServersCache<>(output));
            cache.cancel();
        }, 60 * 60 * 1000L); // 1h

        onlineStatusCache = new Cache<>(cache -> {
            HashMap<Integer, Boolean> output = new HashMap<>();

            CommonMain.instance.getServers().forEach(server ->
                    output.put(server.id, server.isOnline()));

            cache.update(new ServersCache<>(output));
            cache.cancel();
        }, 20 * 1000L); // 20s
        */

        Logger.good("Caching enabled!");
    }

}
