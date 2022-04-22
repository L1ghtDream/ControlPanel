package dev.lightdream.common.manager;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.redis.command.RedisCommand;
import dev.lightdream.common.dto.redis.command.impl.ExecuteCommand;
import dev.lightdream.logger.Debugger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisManager {

    public Jedis jedis;
    private final Jedis subscriberJedis;
    private JedisPubSub subscriberJedisPubSub;

    public RedisManager() {
        this.jedis = new Jedis(CommonMain.instance.redisConfig.host, CommonMain.instance.redisConfig.port);
        this.jedis.auth(CommonMain.instance.redisConfig.username, CommonMain.instance.redisConfig.password);

        this.subscriberJedis = new Jedis(CommonMain.instance.redisConfig.host, CommonMain.instance.redisConfig.port);
        this.subscriberJedis.auth(CommonMain.instance.redisConfig.username, CommonMain.instance.redisConfig.password);

        subscribe();
    }

    private void subscribe() {
        subscriberJedisPubSub = new JedisPubSub() {

            @Override
            public void onMessage(String channel, String command) {
                Debugger.info("[Receive] [" + channel + "] " + command);
                //command = Utils.base64Decode(command);
                //Debugger.info("[Receive] [" + channel + "] [ASCII]  " + command);
                Class<? extends RedisCommand> clazz = Globals.gson.fromJson(command, RedisCommand.class).getClassByName();
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

    public void send(RedisCommand command) {
        Debugger.log("[Send]   [" + CommonMain.instance.redisConfig.channel + "] " + command);
        //Debugger.log("[Send]   [" + CommonMain.instance.redisConfig.channel + "] [Base64] " + Utils.base64Encode(command.toString()));
        jedis.publish(CommonMain.instance.redisConfig.channel, command.toString());
    }


}