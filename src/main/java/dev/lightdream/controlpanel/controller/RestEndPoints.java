package dev.lightdream.controlpanel.controller;

import dev.lightdream.controlpanel.database.Node;
import dev.lightdream.controlpanel.database.Server;
import dev.lightdream.controlpanel.dto.Command;
import dev.lightdream.controlpanel.dto.data.Cookie;
import dev.lightdream.controlpanel.dto.data.LoginData;
import dev.lightdream.controlpanel.dto.request.ServerRequest;
import dev.lightdream.controlpanel.dto.response.Response;
import dev.lightdream.controlpanel.utils.Globals;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import static dev.lightdream.controlpanel.utils.Utils.getServer;

@Slf4j
@Controller
public class RestEndPoints {

    public RestEndPoints() {

    }

    @MessageMapping("/server/api/server")
    public Response console(Command command, @RequestBody ServerRequest serverRequest) {
        if (!serverRequest.cookie.validate()) {
            return Response.UNAUTHORISED();
        }

        if (command.command.equals("start")) {
            Server server = getServer(command.server);
            Node node = server.node;

            if (!node.sendCommand("screen -ls " + server.serverID).contains("No Sockets found")) {
                return Response.LOCKED("Server is already running");
            }

            node.sendCommand(Globals.SERVER_START_CMD
                    .parse("path", server.path)
                    .parse("id", server.serverID)
                    .parse()
            );

            return Response.OK();
        }

        getServer(command.server).sendCommand(command.command);

        return Response.OK();
    }

    @PostMapping("/api/login")
    @ResponseBody
    public Response login(@RequestBody LoginData loginData) {
        Cookie cookie = loginData.generateCookie();

        if (cookie == null) {
            return Response.UNAUTHORISED("Invalid username or password");
        }

        return Response.OK(cookie);
    }

    @PostMapping("/api/login/cookie")
    @ResponseBody
    public Response loginCookie(@RequestBody Cookie cookie) {
        if (cookie.validate()) {
            return Response.OK();
        }
        return Response.UNAUTHORISED("Invalid username or password");
    }

}