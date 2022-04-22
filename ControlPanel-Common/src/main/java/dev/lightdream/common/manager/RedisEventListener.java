package dev.lightdream.common.manager;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.annotations.RedisEventHandler;
import dev.lightdream.common.dto.redis.event.RedisEvent;
import dev.lightdream.logger.Debugger;
import dev.lightdream.logger.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class RedisEventListener {


    @SuppressWarnings("unchecked")
    public RedisEventListener() {
        for (Method method : getClass().getMethods()) {
            if (!method.isAnnotationPresent(RedisEventHandler.class)) {
                continue;
            }

            if (method.getParameterCount() != 1) {
                Logger.error("Method " + getClass().getSimpleName() + "#" + method.getName() + " from does not have the correct parameter count.");
                continue;
            }

            Parameter parameter = method.getParameters()[0];
            Class<?> clazz = parameter.getType();

            Debugger.log(clazz);

            CommonMain.instance.redisEventManager.registerListener((Class<? extends RedisEvent>) clazz, obj -> {
                try {
                    Debugger.log(obj.getClass());
                    Debugger.log(obj.getClassByName());
                    method.invoke(this, clazz.cast(obj));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });

        }
    }

}
