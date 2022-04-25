package dev.lightdream.common.manager;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.redis.RedisResponse;
import dev.lightdream.common.dto.redis.event.RedisEvent;
import dev.lightdream.common.dto.redis.event.impl.ResponseEvent;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.logger.Debugger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RedisManager {

    private final Jedis subscriberJedis;
    private final List<RedisResponse> awaitingResponses = new ArrayList<>();
    public Jedis jedis;
    public int id = 0;
    public String listenID;
    private JedisPubSub subscriberJedisPubSub;

    public RedisManager(String id) {
        Debugger.info("Creating RedisManager with listenID: " + id);
        this.listenID = id;
        this.jedis = new Jedis(CommonMain.instance.redisConfig.host, CommonMain.instance.redisConfig.port);
        this.jedis.auth(CommonMain.instance.redisConfig.username, CommonMain.instance.redisConfig.password);

        this.subscriberJedis = new Jedis(CommonMain.instance.redisConfig.host, CommonMain.instance.redisConfig.port);
        this.subscriberJedis.auth(CommonMain.instance.redisConfig.username, CommonMain.instance.redisConfig.password);

        subscribe();
    }

    @Nullable
    private RedisResponse getResponse(ResponseEvent command) {
        return awaitingResponses.stream().filter(response -> response.id == command.id).findAny().orElse(null);
    }

    private void subscribe() {
        subscriberJedisPubSub = new JedisPubSub() {

            @Override
            public void onMessage(String channel, String command) {
                Class<? extends RedisEvent> clazz = Utils.fromJson(command, RedisEvent.class).getClassByName();

                if (clazz.equals(ResponseEvent.class)) {
                    ResponseEvent responseEvent = Utils.fromJson(command, ResponseEvent.class);
                    if (!responseEvent.redisTarget.equals(listenID)) {
                        Debugger.info("[Receive-Not-Allowed] [" + channel + "] HIDDEN");
                        return;
                    }

                    Debugger.info("[Receive-Response   ] [" + channel + "] " + command);
                    Debugger.log("Received response @ " + System.currentTimeMillis());
                    RedisResponse response = getResponse(responseEvent);
                    Debugger.log("[2] @ " + System.currentTimeMillis());
                    if (response == null) {
                        return;
                    }
                    Debugger.log("[3] @ " + System.currentTimeMillis());
                    response.respond(responseEvent.response);
                    Debugger.log("[7] @ " + System.currentTimeMillis());
                    return;
                }

                RedisEvent redisEvent = Utils.fromJson(command, clazz);
                if (!redisEvent.redisTarget.equals(listenID)) {
                    Debugger.info("[Receive-Not-Allowed] [" + channel + "] HIDDEN");
                    return;
                }
                Debugger.info("[Receive            ] [" + channel + "] " + command);
                redisEvent.fireEvent();
            }

            @Override
            public void onSubscribe(String channel, int subscribedChannels) {
                Debugger.info("Subscribed to channel " + channel);
            }

            @Override
            public void onUnsubscribe(String channel, int subscribedChannels) {
                Debugger.info("Unsubscribed from channel " + channel);
            }

        };

        new Thread(() -> subscriberJedis.subscribe(subscriberJedisPubSub, CommonMain.instance.redisConfig.channel)).start();

    }

    @SuppressWarnings("unused")
    public void unsubscribe() {
        subscriberJedisPubSub.unsubscribe();
    }

    public RedisResponse send(RedisEvent command) {
        command.originator = listenID;

        if (command instanceof ResponseEvent) {
            Debugger.info("[Send-Response      ] [" + CommonMain.instance.redisConfig.channel + "] " + command);
            jedis.publish(CommonMain.instance.redisConfig.channel, command.toString());
            return null;
        }

        command.id = ++id;
        Debugger.info("[Send               ] [" + CommonMain.instance.redisConfig.channel + "] " + command);

        RedisResponse redisResponse = new RedisResponse(command.id);
        jedis.publish(CommonMain.instance.redisConfig.channel, command.toString());

        awaitingResponses.add(redisResponse);

        return redisResponse;
    }


}