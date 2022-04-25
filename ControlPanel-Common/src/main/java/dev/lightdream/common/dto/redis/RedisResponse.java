package dev.lightdream.common.dto.redis;

import com.google.gson.annotations.Expose;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.logger.Debugger;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RedisResponse {

    public int id;
    @Expose
    private String response;
    private boolean finished = false;
    private boolean timeout = false;

    public RedisResponse(int id) {
        this.id = id;
    }

    public void markAsFinished() {
        finished = true;
    }

    public void timeout() {
        timeout = true;
    }

    public boolean hasTimeout() {
        return timeout;
    }

    public void respond(Object object) {
        Debugger.log("[4] @ " + System.currentTimeMillis());
        this.response = Utils.toJson(object);
        Debugger.log("[5] @ " + System.currentTimeMillis());
        markAsFinished();
        Debugger.log("[6] @ " + System.currentTimeMillis());
    }

    public <T> T getResponse(Class<T> clazz) {
        if (response == null) {
            return null;
        }
        return Utils.fromJson(
                response.replace("\\\"", "\\")
                        .replace("\"", "")
                        .replace("\\", "\"")
                , clazz);
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public String toString() {
        return Utils.toJson(this);
    }
}
