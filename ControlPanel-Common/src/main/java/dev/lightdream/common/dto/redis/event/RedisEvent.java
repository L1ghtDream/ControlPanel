package dev.lightdream.common.dto.redis.event;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.redis.RedisResponse;
import dev.lightdream.common.dto.redis.event.impl.ResponseEvent;
import dev.lightdream.common.utils.Utils;
import lombok.SneakyThrows;

public class RedisEvent {

    public int id = -1;
    public String className;
    public String originator = "UNKNOWN";
    public String redisTarget = "*";

    /**
     * @param redisTarget the redis target that will listen for this event. You can use * for all.
     */
    public RedisEvent(String redisTarget) {
        this.className = getClass().getName();
        this.redisTarget = redisTarget;
    }

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
        ResponseEvent responseEvent = new ResponseEvent(this, response);
        CommonMain.instance.redisManager.send(responseEvent);
    }

    @SuppressWarnings("UnusedReturnValue")
    public RedisResponse send() {
        return CommonMain.instance.redisManager.send(this);
    }

    @SuppressWarnings("BusyWait")
    @SneakyThrows
    public RedisResponse sendAndWait() {
        RedisResponse response = CommonMain.instance.redisManager.send(this);
        while (!response.isFinished()) {
            Thread.sleep(100);
        }
        return response;
    }

}
