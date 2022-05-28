package dev.lightdream.controlpanel.config;

import dev.lightdream.common.database.Server;
import dev.lightdream.common.database.User;
import dev.lightdream.common.dto.Command;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.utils.AuthUtils;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.controlpanel.Main;
import dev.lightdream.controlpanel.service.ConsoleService;
import dev.lightdream.logger.Debugger;
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
        Debugger.log(1);
        StompCommand command = (StompCommand) message.getHeaders().get("stompCommand");

        Debugger.log(2);
        if (command == null) {
            throw new IllegalArgumentException("403");
        }

        Debugger.log(3);
        if (command.equals(StompCommand.DISCONNECT)) {
            return message;
        }

        Debugger.log(4);
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);

        Debugger.log(5);
        List<String> usernames = headers.getNativeHeader("username");
        List<String> passwords = headers.getNativeHeader("password");

        Debugger.log(6);
        Debugger.log("[SubscriptionInterceptor#preSend] usernames: " + usernames);
        Debugger.log("[SubscriptionInterceptor#preSend] passwords: " + passwords);

        Debugger.log(7);
        if (usernames == null || passwords == null) {
            throw new IllegalArgumentException("401. Null credentials");
        }

        Debugger.log(8);
        String username = usernames.get(0);
        String password = passwords.get(0);

        Debugger.log(9);
        if (command.equals(StompCommand.CONNECT)) {
            Debugger.log(10);
            if (!validateConnection(username, password)) {
                Debugger.log(11);
                throw new IllegalArgumentException("401. Invalid credentials for connect");
            }
            Debugger.log(12);
        }

        if (command.equals(StompCommand.SUBSCRIBE)) {
            List<String> servers = headers.getNativeHeader("server");
            String server = servers.get(0);

            if (!validateSubscription(username, password, server)) {
                throw new IllegalArgumentException("401. Invalid credentials for subscribe");
            }
            sendCurrentLog(username, password, server);
        }

        if (command.equals(StompCommand.SEND)) {
            if (!validateCommand(username, password, Utils.payloadToString(message))) {
                throw new IllegalArgumentException("401. Invalid credentials for send");
            }
        }

        return message;
    }

    private boolean validateConnection(String username, String password) {
        Debugger.log(20);
        if (password == null) {
            Debugger.log(21);
            return false;
        }

        Debugger.log(22);
        Debugger.log(username);
        Debugger.log(password);
        return AuthUtils.checkHash(username, password);
    }

    private boolean validateSubscription(String username, String password, String serverID) {
        if (password == null || serverID == null) {
            return false;
        }

        // /server/{server}/api/console
        //String serverName = destination.split("/")[2];

        User user = User.getUser(username);
        Server server = Utils.getServer(serverID);

        //return user.hasPermission(server, PermissionEnum.SERVER_CONSOLE);
        return true;
    }

    private boolean validateCommand(String username, String password, String commandJson) {
        if (password == null || commandJson == null) {
            return false;
        }

        Command command = Utils.fromJson(commandJson, Command.class);
        User user = User.getUser(username);
        Server server = command.getServer();

        if (command.isServerCommand()) {
            return user.hasPermission(server, PermissionEnum.SERVER_CONTROL);
        }

        return user.hasPermission(server, PermissionEnum.SERVER_CONSOLE);
    }

    @SuppressWarnings("unused")
    private void sendCurrentLog(String username, String password, String serverID) {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {

                        // /server/{server}/api/console
                        //String serverName = destination.split("/")[2];

                        Server server = Utils.getServer(serverID);

                        ConsoleService.instance.sendConsole(server, Main.instance.logManager.getLog(server));
                    }
                },
                500
        );
    }
}