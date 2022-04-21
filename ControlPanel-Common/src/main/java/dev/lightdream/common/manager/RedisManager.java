package dev.lightdream.common.manager;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.Node;
import dev.lightdream.logger.Debugger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisManager {

    public Jedis jedis;
    private Jedis subscriberJedis;
    private JedisPubSub subscriberJedisPubSub;
    public CommonMain main;

    public RedisManager(CommonMain main) {
        this.main = main;

        this.jedis = new Jedis(main.redisConfig.host, main.redisConfig.port);
        this.jedis.auth(main.redisConfig.username, main.redisConfig.password);

        if (main.subscribeRedis()) {
            this.subscriberJedis = new Jedis(main.redisConfig.host, main.redisConfig.port);
            this.subscriberJedis.auth(main.redisConfig.username, main.redisConfig.password);

            subscribe();
        }
    }

    private void subscribe() {
        subscriberJedisPubSub = new JedisPubSub() {

            @Override
            public void onMessage(String channel, String command) {
                Debugger.info("[" + channel + "] " + command);
                Node.executeCommandLocal(command);
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

    public void send(String message){
        jedis.publish(main.redisConfig.channel, message);
    }


}