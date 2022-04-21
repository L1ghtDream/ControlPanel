package dev.lightdream.common.manager;

import com.google.gson.Gson;
import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.redis.command.RedisCommand;
import dev.lightdream.logger.Debugger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisManager {

    public Jedis jedis;
    private final Jedis subscriberJedis;
    private JedisPubSub subscriberJedisPubSub;
    public CommonMain main;

    public RedisManager(CommonMain main) {
        this.main = main;

        Debugger.log(main.redisConfig.host);

        this.jedis = new Jedis(main.redisConfig.host, main.redisConfig.port);
        this.jedis.auth(main.redisConfig.username, main.redisConfig.password);

        this.subscriberJedis = new Jedis(main.redisConfig.host, main.redisConfig.port);
        this.subscriberJedis.auth(main.redisConfig.username, main.redisConfig.password);

        subscribe();
    }

    private void subscribe() {
        subscriberJedisPubSub = new JedisPubSub() {

            @Override
            public void onMessage(String channel, String command) {
                Debugger.info("[" + channel + "] " + command);
                new Gson().fromJson(command, RedisCommand.class).fireEvent();
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

        new Thread(() -> subscriberJedis.subscribe(subscriberJedisPubSub, main.redisConfig.channel)).start();

    }

    @SuppressWarnings("unused")
    public void unsubscribe() {
        subscriberJedisPubSub.unsubscribe();
    }

    public void send(RedisCommand command) {
        jedis.publish(main.redisConfig.channel, command.toString());
    }


}