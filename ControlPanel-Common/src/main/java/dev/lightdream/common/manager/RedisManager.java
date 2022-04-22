package dev.lightdream.common.manager;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.redis.RedisResponse;
import dev.lightdream.common.dto.redis.command.RedisCommand;
import dev.lightdream.common.dto.redis.command.impl.RedisResponseCommand;
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
    private JedisPubSub subscriberJedisPubSub;

    public RedisManager() {
        this.jedis = new Jedis(CommonMain.instance.redisConfig.host, CommonMain.instance.redisConfig.port);
        this.jedis.auth(CommonMain.instance.redisConfig.username, CommonMain.instance.redisConfig.password);

        this.subscriberJedis = new Jedis(CommonMain.instance.redisConfig.host, CommonMain.instance.redisConfig.port);
        this.subscriberJedis.auth(CommonMain.instance.redisConfig.username, CommonMain.instance.redisConfig.password);

        subscribe();
    }

    @Nullable
    private RedisResponse getResponse(RedisResponseCommand command) {
        return awaitingResponses.stream().filter(response -> response.id == command.id).findAny().orElse(null);
    }

    private void subscribe() {
        subscriberJedisPubSub = new JedisPubSub() {

            @Override
            public void onMessage(String channel, String command) {
                Class<? extends RedisCommand> clazz = Globals.gson.fromJson(command, RedisCommand.class).getClassByName();

                if (clazz.equals(RedisResponseCommand.class)) {
                    Debugger.info("[Receive-Response] [" + channel + "] " + command);
                    RedisResponseCommand responseCommand = Globals.gson.fromJson(command, RedisResponseCommand.class);
                    RedisResponse response = getResponse(responseCommand);
                    if (response == null) {
                        return;
                    }
                    response.respond(responseCommand.response);
                    return;
                }

                Debugger.info("[Receive         ] [" + channel + "] " + command);
                Globals.gson.fromJson(command, clazz).fireEvent();
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

    public RedisResponse send(RedisCommand command) {
        if (command instanceof RedisResponseCommand) {
            Debugger.log("[Send-Response   ] [" + CommonMain.instance.redisConfig.channel + "] " + command);
            jedis.publish(CommonMain.instance.redisConfig.channel, command.toString());
            return null;
        }

        command.id = ++id;
        Debugger.log("[Send            ] [" + CommonMain.instance.redisConfig.channel + "] " + command);

        RedisResponse redisResponse = new RedisResponse(command.id);
        jedis.publish(CommonMain.instance.redisConfig.channel, command.toString());

        awaitingResponses.add(redisResponse);

        return redisResponse;
    }


}