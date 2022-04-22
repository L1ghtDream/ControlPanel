package dev.lightdream.common.dto.redis.event;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.redis.event.impl.ResponseEvent;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.logger.Debugger;
import lombok.SneakyThrows;

public class RedisEvent {

    public int id = -1;
    public String className;

    public RedisEvent() {
        this.className = getClass().getName();
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public Class<? extends RedisEvent> getClassByName() {
        return (Class<? extends RedisEvent>) Class.forName(className);
    }

    public void fireEvent() {
        CommonMain.instance.redisEventManager.fire(this);
    }

    @Override
    public String toString() {
        return Utils.toJson(this);
    }

    public void respond(Object response) {
        Debugger.log("Event id: " + id);
        ResponseEvent responseEvent = new ResponseEvent(this, response);
        Debugger.log("Response id: " + responseEvent.id);
        CommonMain.instance.redisManager.send(responseEvent);
    }
}
