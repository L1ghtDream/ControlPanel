package dev.lightdream.controlpanel.controller;

import dev.lightdream.common.sftp.database.Node;
import dev.lightdream.common.sftp.database.Server;
import dev.lightdream.common.sftp.dto.data.Cookie;
import dev.lightdream.common.sftp.utils.Globals;
import dev.lightdream.common.sftp.utils.Utils;
import dev.lightdream.controlpanel.dto.Command;
import dev.lightdream.controlpanel.dto.data.LoginData;
import dev.lightdream.controlpanel.dto.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class RestEndPoints {

    public RestEndPoints() {

    }

    @MessageMapping("/server/api/server")
    public Response console(Command command) {
        if (!command.cookie.validate()) {
            return Response.UNAUTHORISED();
        }

        if (command.getCommand().equals("start")) {
            Server server = Utils.getServer(command.server);
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

        Utils.getServer(command.server).sendCommand(command.getCommand());

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