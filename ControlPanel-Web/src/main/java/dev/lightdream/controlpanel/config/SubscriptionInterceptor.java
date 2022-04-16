package dev.lightdream.controlpanel.config;

import com.google.gson.Gson;
import dev.lightdream.common.database.Server;
import dev.lightdream.common.database.User;
import dev.lightdream.common.dto.data.Cookie;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.controlpanel.dto.Command;
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
        StompCommand command = (StompCommand) message.getHeaders().get("stompCommand");

        if (command == null) {
            throw new IllegalArgumentException("403");
        }

        if (command.equals(StompCommand.DISCONNECT)) {
            return message;
        }

        StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);

        List<String> usernames = headers.getNativeHeader("username");
        List<String> passwords = headers.getNativeHeader("password");

        if (usernames == null || passwords == null) {
            throw new IllegalArgumentException("401");
        }

        String user = usernames.get(0);
        String password = passwords.get(0);

        if (command.equals(StompCommand.CONNECT)) {
            if (!validateConnection(user, password)) {
                throw new IllegalArgumentException("401");
            }
        }

        if (command.equals(StompCommand.SUBSCRIBE)) {
            if (!validateSubscription(user, password, headers.getDestination())) {
                throw new IllegalArgumentException("401");
            }
        }

        if (command.equals(StompCommand.SEND)) {
            if (!validateCommand(user, password, headers.getDestination(), Utils.payloadToString(message))) {
                throw new IllegalArgumentException("401");
            }
        }

        return message;
    }

    private boolean validateConnection(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        Cookie cookie = new Cookie(username, password);

        return cookie.validate();
    }

    private boolean validateSubscription(String username, String password, String destination) {
        if (username == null || password == null || destination == null) {
            return false;
        }

        // /server/{server}/api/console
        String serverName = destination.split("/")[2];

        Cookie cookie = new Cookie(username, password);
        User user = cookie.getUser();
        Server server = Utils.getServer(serverName);

        return user.hasPermission(server, PermissionEnum.SERVER_CONSOLE);
    }

    private boolean validateCommand(String username, String password, String destination, String commandJson) {
        if (username == null || password == null || destination == null || commandJson == null) {
            return false;
        }

        // /server/{server}/api/console
        String serverName = destination.split("/")[2];

        Cookie cookie = new Cookie(username, password);
        User user = cookie.getUser();
        Server server = Utils.getServer(serverName);

        Command command = new Gson().fromJson(commandJson, Command.class);
        if (command.isServerCommand()) {
            return user.hasPermission(server, PermissionEnum.SERVER_CONTROL);
        }

        return user.hasPermission(server, PermissionEnum.SERVER_CONSOLE);
    }


}