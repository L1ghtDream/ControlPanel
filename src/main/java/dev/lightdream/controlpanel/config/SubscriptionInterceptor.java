package dev.lightdream.controlpanel.config;

import dev.lightdream.logger.Debugger;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.HashMap;

public class SubscriptionInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        Debugger.log("[1] " + headerAccessor.getNativeHeader("username"));
        Debugger.log("[2] " + headerAccessor.getNativeHeader("password"));

        String user = null;
        String password = null;

        Debugger.log("Initial: " + user + " : " + password);

        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            if (!validateConnection(user, password)) {
                Debugger.log("Error 3");
                throw new IllegalArgumentException("401");
            }
        }

        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            if (!validateSubscription(user, password, headerAccessor.getDestination())) {
                Debugger.log("Error 4");
                throw new IllegalArgumentException("401");
            }
        }

        if (StompCommand.SEND.equals(headerAccessor.getCommand())) {
            //Debugger.log(headerAccessor);
        }

        return message;
    }

    private boolean validateConnection(String user, String password) {

        Debugger.log(user + " : " + password);

        return user != null && password != null;
    }

    private boolean validateSubscription(String user, String password, String destination) {

        Debugger.log(user + " : " + password + " @ " + destination);

        return user != null && password != null && destination != null;
    }
}