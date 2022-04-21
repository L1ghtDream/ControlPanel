package dev.lightdream.common.dto.redis.event;

import dev.lightdream.common.dto.redis.command.impl.PublicKeySend;
import dev.lightdream.lambda.LambdaExecutor;

import java.util.ArrayList;
import java.util.List;

public class PublicKeyReceive extends PublicKeySend implements RedisEvent {

    private static final List<LambdaExecutor.NoReturnLambdaExecutor<PublicKeyReceive>> executors = new ArrayList<>();

    public PublicKeyReceive(PublicKeySend data) {
        this.nodeID = data.nodeID;
        this.publicKey = data.publicKey;
    }

    public void fire() {
        for (LambdaExecutor.NoReturnLambdaExecutor<PublicKeyReceive> executor : executors) {
            executor.execute(this);
        }
    }

    public static void registerListener(LambdaExecutor.NoReturnLambdaExecutor<PublicKeyReceive> executor) {
        executors.add(executor);
    }


}
