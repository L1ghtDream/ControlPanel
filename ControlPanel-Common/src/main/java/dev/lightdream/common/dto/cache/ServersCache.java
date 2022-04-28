package dev.lightdream.common.dto.cache;

import dev.lightdream.common.database.Server;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
public class ServersCache<T> {
    public HashMap<String, T> servers = new HashMap<>();
    public T defaultValue = null;

    public ServersCache(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public T get(Server server) {
        return servers.getOrDefault(server.getID(), defaultValue);
    }

    public void set(Server server, T value) {
        servers.put(server.getID(), value);
    }

    public void set(ServersCache<T> serversCache) {
        servers.putAll(serversCache.servers);
    }
}