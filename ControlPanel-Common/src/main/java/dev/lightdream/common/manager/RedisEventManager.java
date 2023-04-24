package dev.lightdream.common.manager;

import dev.lightdream.common.dto.redis.event.RedisEvent;
import dev.lightdream.lambda.lambda.ArgLambdaExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RedisEventManager {

    public HashMap<Class<? extends RedisEvent>, List<ArgLambdaExecutor<RedisEvent>>> eventListeners = new HashMap<>();

    public void registerListener(Class<? extends RedisEvent> event, ArgLambdaExecutor<RedisEvent> listener) {
        List<ArgLambdaExecutor<RedisEvent>> listeners = new ArrayList<>(eventListeners.getOrDefault(event, new ArrayList<>()));
        listeners.add(listener);
        eventListeners.put(event, listeners);
    }

    public void fire(RedisEvent event) {
        eventListeners.getOrDefault(event.getClass(), new ArrayList<>())
                .forEach(listener -> listener.execute(event));
    }

}
