package dev.lightdream.controlpanel.controller.rest_end_points;

import dev.lightdream.common.database.Node;
import dev.lightdream.common.dto.Command;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.dto.response.Response;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.controlpanel.Main;
import dev.lightdream.controlpanel.controller.RestEndPoints;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ServerRest extends RestEndPoints {

    public static ServerRest instance;

    public ServerRest() {
        if (instance == null) {
            instance = this;
        }
    }

    @MessageMapping("/server/api/server")
    @ResponseBody
    public Response console(HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64,
                            @RequestBody Command command) {
        dev.lightdream.common.database.Server server = Utils.getServer(command.server);

        return executeEndPoint(request, cookieBase64,
                (user) -> {
                    if (command.getCommand().equals("start")) {
                        if (user.hasPermission(server, PermissionEnum.SERVER_CONTROL)) {
                            return Response.UNAUTHORISED();
                        }

                        Node node = server.node;

                        String response = node.executeCommand("screen -ls " + server.id);

                        if (response.contains("No Sockets found") ||
                                response.equals("")) {
                            node.executeCommand(Main.instance.config.SERVER_START_CMD
                                    .parse("path", server.path)
                                    .parse("id", server.id)
                                    .parse()
                            );

                            return Response.OK();
                        }

                        return Response.LOCKED("Server is already running");
                    }

                    if (command.getCommand().equals("start") ||
                            command.getCommand().equals("stop") ||
                            command.getCommand().equals("restart") ||
                            command.getCommand().equals("kill")) {

                        if (user.hasPermission(server, PermissionEnum.SERVER_CONTROL)) {
                            return Response.UNAUTHORISED();
                        }
                    }
                    server.sendCommand(command.getCommand());


                    return Response.OK();
                },
                server, PermissionEnum.SERVER_CONSOLE);
    }
}
