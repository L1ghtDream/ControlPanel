package dev.lightdream.common.dto.redis.command;

import com.google.gson.Gson;
import dev.lightdream.common.dto.redis.event.RedisEvent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class RedisCommand {

    public abstract RedisEvent getEvent();

    public void fireEvent(){
        getEvent().fire();
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
