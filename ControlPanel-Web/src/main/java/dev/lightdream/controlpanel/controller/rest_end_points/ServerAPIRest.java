package dev.lightdream.controlpanel.controller.rest_end_points;

import dev.lightdream.common.database.Node;
import dev.lightdream.common.database.Server;
import dev.lightdream.common.database.User;
import dev.lightdream.common.dto.Command;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.dto.response.Response;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.controlpanel.Main;
import dev.lightdream.controlpanel.controller.RestEndPoints;
import dev.lightdream.logger.Debugger;
import lombok.SneakyThrows;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ServerAPIRest extends RestEndPoints {

    public static ServerAPIRest instance;

    public ServerAPIRest() {
        if (instance == null) {
            instance = this;
        }
    }

    @SneakyThrows
    @PostMapping("/api/stats/{serverID}")
    @ResponseBody
    public Response stats(HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64,
                          @PathVariable String serverID) {
        Server server = Server.getServer(serverID);

        return executeEndPoint(request, cookieBase64,
                (user) -> {
                    //noinspection CodeBlock2Expr
                    return Response.OK(Main.instance.serversCache.getStats(server));
                },
                server, PermissionEnum.SERVER_VIEW
        );
    }

    @MessageMapping("/server/api/server")
    @ResponseBody
    public Response console(Command command) {

        Debugger.log(1);
        dev.lightdream.common.database.Server server = Utils.getServer(command.server);

        return executeEndPoint(null, Utils.base64Encode(command.cookie.toString()),
                (user) -> {
                    Debugger.log(2);
                    if (command.getCommand().equals("start") ||
                            command.getCommand().equals("stop") ||
                            command.getCommand().equals("restart") ||
                            command.getCommand().equals("kill")) {

                        if (!user.hasPermission(server, PermissionEnum.SERVER_CONTROL)) {
                            return Response.UNAUTHORISED();
                        }
                    }

                    if (command.getCommand().equals("start")) {
                        return handleStart(user, server);
                    }

                    if (command.getCommand().equals("kill")) {
                        return handleKill(user, server);
                    }


                    server.sendCommand(command.getCommand());


                    return Response.OK();
                },
                server, PermissionEnum.SERVER_CONSOLE
        );
    }


    private Response handleStart(User user, Server server) {
        Debugger.log(3);
        if (!user.hasPermission(server, PermissionEnum.SERVER_CONTROL)) {
            Debugger.log(4);
            return Response.UNAUTHORISED();
        }

        Node node = server.node;

        Debugger.log(5);
        String response = node.executeCommand("screen -ls " + server.id);

        if (!response.contains("No Sockets found") && !response.equals("")) {
            return Response.LOCKED("Server is already running");
        }

        Debugger.log(6);
        node.executeCommand(Main.instance.config.SERVER_START_CMD
                .parse("path", server.path)
                .parse("id", server.id)
                .parse()
        );

        return Response.OK();

    }

    private Response handleKill(User user, Server server) {
        if (!user.hasPermission(server, PermissionEnum.SERVER_CONTROL)) {
            return Response.UNAUTHORISED();
        }

        Node node = server.node;

        //String response = node.executeCommand("screen -ls " + server.id);
        node.executeCommand(Main.instance.config.KILL_CMD
                .parse("port", server.port)
                .parse()
        );
        return Response.OK();
    }
}
