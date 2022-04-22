package dev.lightdream.common.dto.cache;

import dev.lightdream.common.database.Server;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
public class ServersCache<T> {
    public HashMap<Integer, T> servers = new HashMap<>();

    @SuppressWarnings("unused")
    public T get(Server server) {
        return servers.get(server.id);
    }

    @SuppressWarnings("unused")
    public void set(Server server, T value) {
        servers.put(server.id, value);
    }

    public void set(ServersCache<T> serversCache) {
        servers.putAll(serversCache.servers);
    }
}