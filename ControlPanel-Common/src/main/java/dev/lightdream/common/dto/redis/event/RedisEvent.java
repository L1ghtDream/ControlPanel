package dev.lightdream.common.dto.redis.event;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.Node;
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

    public RedisEvent(Node redisTarget) {
        this.className = getClass().getName();
        this.redisTarget = redisTarget.getID();
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
        new ResponseEvent(this, response).send();
    }

    @SuppressWarnings("UnusedReturnValue")
    public RedisResponse send() {
        return CommonMain.instance.redisManager.send(this);
    }

    @SneakyThrows
    public RedisResponse sendAndWait() {
        return sendAndWait(Utils.defaultTimeout);
    }

    @SuppressWarnings("BusyWait")
    @SneakyThrows
    public RedisResponse sendAndWait(int timeout) {
        int currentWait = 0;
        RedisResponse response = send();
        while (!response.isFinished()) {
            Thread.sleep(Utils.defaultWaitBeforeIteration);
            currentWait += Utils.defaultWaitBeforeIteration;
            if (currentWait > timeout) {
                response.timeout();
                break;
            }
        }

        return response;
    }

}
