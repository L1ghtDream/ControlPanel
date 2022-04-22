package dev.lightdream.common.dto.redis;

import dev.lightdream.common.manager.Globals;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RedisResponse {

    public int id;
    public Object response;
    private boolean finished = false;

    public RedisResponse(int id) {
        this.id = id;
    }

    public void markAsFinished() {
        finished = true;
    }

    public void respond(Object object) {
        this.response = object;
        markAsFinished();
        // TODO send to redis
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public String toString() {
        return Globals.gson.toJson(this);
    }
}
