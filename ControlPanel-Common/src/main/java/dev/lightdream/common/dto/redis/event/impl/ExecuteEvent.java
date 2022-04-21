package dev.lightdream.common.dto.redis.event.impl;

import dev.lightdream.common.dto.redis.command.impl.ExecuteCommand;
import dev.lightdream.common.dto.redis.event.RedisEvent;
import dev.lightdream.lambda.LambdaExecutor;

import java.util.ArrayList;
import java.util.List;

public class ExecuteEvent extends ExecuteCommand implements RedisEvent {
    private static final List<LambdaExecutor.NoReturnLambdaExecutor<ExecuteCommand>> executors = new ArrayList<>();

    public ExecuteEvent(ExecuteCommand data) {
        this.className = data.className;
        this.command = data.command;
    }

    public void fire() {
        for (LambdaExecutor.NoReturnLambdaExecutor<ExecuteCommand> executor : executors) {
            executor.execute(this);
        }
    }

    public static void registerListener(LambdaExecutor.NoReturnLambdaExecutor<ExecuteCommand> executor) {
        executors.add(executor);
    }
}
