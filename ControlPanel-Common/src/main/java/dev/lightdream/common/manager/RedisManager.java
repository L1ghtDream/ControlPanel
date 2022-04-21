package dev.lightdream.common.manager;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.Node;
import dev.lightdream.logger.Debugger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisManager {

    public Jedis jedis;
    public Jedis subscriberJedis;
    public JedisPubSub subscriberJedisPubSub;
    public CommonMain main;

    public RedisManager(CommonMain main) {
        this.main = main;

        this.jedis = new Jedis("redis-13050.c135.eu-central-1-1.ec2.cloud.redislabs.com", 13050);
        this.jedis.auth("default", "3Ud7sUzQjRc6YiFBRvEJyeBlCPWBf5qY");

        this.subscriberJedis = new Jedis("redis-13050.c135.eu-central-1-1.ec2.cloud.redislabs.com", 13050);
        this.subscriberJedis.auth("default", "3Ud7sUzQjRc6YiFBRvEJyeBlCPWBf5qY");

        registerCommunicationChannel();
    }

    public void registerCommunicationChannel() {
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

        new Thread(() -> subscriberJedis.subscribe(subscriberJedisPubSub, "control_panel")).start();

    }

    @SuppressWarnings("unused")
    public void unsubscribe() {
        subscriberJedisPubSub.unsubscribe();
    }


}