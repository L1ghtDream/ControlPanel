package dev.lightdream.common.manager;

import dev.lightdream.common.dto.redis.event.PublicKeyReceive;

public class RedisEventListener {

    public RedisEventListener() {
        PublicKeyReceive.registerListener(event -> {
            System.out.println("PublicKeyReceive event: " + event.publicKey);
        });
    }

}
