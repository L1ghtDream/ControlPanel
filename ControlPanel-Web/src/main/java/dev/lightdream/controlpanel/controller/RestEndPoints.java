package dev.lightdream.controlpanel.controller;

import com.google.common.hash.Hashing;
import dev.lightdream.common.database.Node;
import dev.lightdream.common.database.Server;
import dev.lightdream.common.dto.Command;
import dev.lightdream.common.dto.data.Cookie;
import dev.lightdream.common.dto.data.LoginData;
import dev.lightdream.common.dto.response.Response;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.controlpanel.Main;
import dev.lightdream.logger.Debugger;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

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

        Utils.getServer(command.server).sendCommand(command.getCommand());

        return Response.OK();
    }

    @PostMapping("/api/login")
    @ResponseBody
    public Response login(@RequestBody LoginData loginData) {
        loginData.password = Hashing.sha256()
                .hashString(loginData.password, StandardCharsets.UTF_8)
                .toString();
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

    @SneakyThrows
    @PostMapping("/api/stats/{serverID}")
    @ResponseBody
    public Response cpuUsage(@PathVariable String serverID) {
        Server server = Server.getServer(serverID);
        Debugger.log("Getting stats for server " + serverID);
        return Response.OK(Main.instance.serversCache.getStats(server));
    }

    @PostMapping("/api/cookie-check")
    public Response cookieCheck(@CookieValue(value = "login_data") String cookieBase64) {
        Cookie cookie = Utils.getCookie(cookieBase64);
        if (cookie == null) {
            return Response.UNAUTHORISED();
        }
        return Response.OK();
    }

}