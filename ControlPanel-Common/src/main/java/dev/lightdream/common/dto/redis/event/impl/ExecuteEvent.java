package dev.lightdream.common.dto.redis.event.impl;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.redis.command.impl.ExecuteCommand;
import dev.lightdream.common.dto.redis.command.impl.RedisResponseCommand;
import dev.lightdream.common.dto.redis.event.RedisEvent;
import dev.lightdream.lambda.LambdaExecutor;
import dev.lightdream.logger.Debugger;

import java.util.ArrayList;
import java.util.List;

public class ExecuteEvent extends ExecuteCommand implements RedisEvent {
    private static final List<LambdaExecutor.NoReturnLambdaExecutor<ExecuteEvent>> executors = new ArrayList<>();

    public ExecuteEvent(ExecuteCommand data) {
        this.id=data.id;
        this.className = data.className;
        this.command = data.command;
    }

    public static void registerListener(LambdaExecutor.NoReturnLambdaExecutor<ExecuteEvent> executor) {
        executors.add(executor);
    }

    public void respond(Object response) {
        Debugger.log("Event id: " + id);
        RedisResponseCommand responseCommand = new RedisResponseCommand(this, response);
        Debugger.log("Response id: " + responseCommand.id);
        CommonMain.instance.redisManager.send(responseCommand);
    }

    public void fire() {
        for (LambdaExecutor.NoReturnLambdaExecutor<ExecuteEvent> executor : executors) {
            executor.execute(this);
        }
    }
}
