package dev.lightdream.common.manager;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.redis.RedisResponse;
import dev.lightdream.common.dto.redis.event.RedisEvent;
import dev.lightdream.common.dto.redis.event.impl.ResponseEvent;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.logger.Debugger;
import dev.lightdream.logger.Logger;
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

        String username = CommonMain.instance.redisConfig.username;
        String password = CommonMain.instance.redisConfig.password;
        String host = CommonMain.instance.redisConfig.host;
        int port = CommonMain.instance.redisConfig.port;

        Debugger.log("Connecting to redis server on " + username + "@" + host + ":" + port);

        this.jedis = new Jedis(host, port);
        this.jedis.auth(username, password);

        this.subscriberJedis = new Jedis(host, port);
        this.subscriberJedis.auth(username, password);

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
                    RedisResponse response = getResponse(responseEvent);
                    if (response == null) {
                        return;
                    }
                    response.respond(responseEvent.response);
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


        startRedisThread();
    }

    public void startRedisThread() {
        new Thread(() -> {
            try {
                subscriberJedis.subscribe(subscriberJedisPubSub, CommonMain.instance.redisConfig.channel);
            } catch (Exception e) {
                try {
                    Logger.error("Lost connection to redis server. Retrying in 3 seconds...");
                    Thread.sleep(3000);
                    Logger.good("Reconnected to redis server.");
                    startRedisThread();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }).start();
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