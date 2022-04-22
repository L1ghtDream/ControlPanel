package dev.lightdream.common.dto.redis.command;

import dev.lightdream.common.dto.redis.event.RedisEvent;
import dev.lightdream.common.manager.Globals;
import lombok.SneakyThrows;

public class RedisCommand {

    public int id = -1;
    public String className;

    public RedisCommand() {
        this.className = getClass().getName();
    }

    public RedisEvent getEvent() {
        throw new IllegalArgumentException(getClassByName().getSimpleName() + "#_getEvent() is not implemented");
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public Class<? extends RedisCommand> getClassByName() {
        return (Class<? extends RedisCommand>) Class.forName(className);
    }

    public void fireEvent() {
        getEvent().fire();
    }

    @Override
    public String toString() {
        return Globals.gson.toJson(this);
    }
}
