package dev.lightdream.controlpanel.config;

import dev.lightdream.controlpanel.database.Server;
import dev.lightdream.controlpanel.utils.Utils;
import dev.lightdream.logger.Debugger;
import dev.lightdream.messagebuilder.MessageBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.List;

public class SubscriptionInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel channel) {

        Debugger.log("[1] " + message.getHeaders());
        Debugger.log("[2] " + message.getHeaders().get("stompCommand"));

        StompCommand command = (StompCommand) message.getHeaders().get("stompCommand");

        if (command == null) {
            Debugger.log("Error 6");
            throw new IllegalArgumentException("403");
        }

        if (command.equals(StompCommand.DISCONNECT)) {
            Debugger.log("Disconnect with headers " + message.getHeaders());
            return message;
        }

        StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);

        List<String> usernames = headers.getNativeHeader("username");
        List<String> passwords = headers.getNativeHeader("password");

        if (usernames == null || passwords == null) {
            Debugger.log("Error 5");
            Debugger.log(headers);
            throw new IllegalArgumentException("401");
        }

        String user = usernames.get(0);
        String password = passwords.get(0);

        if (command.equals(StompCommand.CONNECT)) {
            if (!validateConnection(user, password)) {
                Debugger.log("Error 3");
                throw new IllegalArgumentException("401");
            }
        }

        if (command.equals(StompCommand.SUBSCRIBE)) {
            if (!validateSubscription(user, password, headers.getDestination())) {
                Debugger.log("Error 4");
                throw new IllegalArgumentException("401");
            }
        }

        if (command.equals(StompCommand.SEND)) {
            if (!validateCommand(user, password, headers.getDestination(), Utils.payloadToString(message))) {
                Debugger.log("Error 4");
                throw new IllegalArgumentException("401");
            }
        }

        Debugger.log("7");

        return message;
    }

    private boolean validateConnection(String user, String password) {

        if (user == null || password == null) {
            return false;
        }


        Debugger.log(new MessageBuilder("[Connection] %user% : %password%")
                .parse("user", user)
                .parse("password", password)
                .parse()
        );

        return true;
    }

    private boolean validateSubscription(String user, String password, String destination) {

        if (user == null || password == null || destination == null) {
            return false;
        }

        // /server/{server}/api/console
        String serverName = destination.split("/")[2];

        Server server = Utils.getServer(serverName);

        Debugger.log(new MessageBuilder("[Subscription] %user% : %password% @ %server% (%server_url%)")
                .parse("user", user)
                .parse("password", password)
                .parse("server", serverName)
                .parse("server_url", destination)
                .parse()
        );

        return true;
    }

    private boolean validateCommand(String user, String password, String destination, String command) {
        if (user == null || password == null || destination == null || command == null) {
            return false;
        }

        // /server/{server}/api/console
        String serverName = destination.split("/")[2];
        Server server = Utils.getServer(serverName);

        Debugger.log(new MessageBuilder("[Subscription] %user% : %password% @ %server% (%server_url%) - %command%")
                .parse("user", user)
                .parse("password", password)
                .parse("server", serverName)
                .parse("server_url", destination)
                .parse("command", command)
                .parse()
        );

        return true;
    }


}