package dev.lightdream.controlpanel.controller;

import dev.lightdream.controlpanel.dto.Command;
import dev.lightdream.controlpanel.dto.Node;
import dev.lightdream.controlpanel.dto.Server;
import dev.lightdream.controlpanel.dto.response.Response;
import dev.lightdream.controlpanel.utils.Globals;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import static dev.lightdream.controlpanel.utils.Utils.getServer;

@Slf4j
@Controller
public class RestEndPoints {

    public RestEndPoints() {

    }

    @MessageMapping("/server/api/server")
    public Response send(Command command) {
        if (command.command.equals("start")) {
            Server server = getServer(command.server);
            Node node = server.node;

            if (!node.sendCommand("screen -ls " + server.id).contains("No Sockets found")) {
                //TODO Send response to make the start button not clickable and send message that the server is already running
                return Response.OK();
            }

            node.sendCommand(Globals.SERVER_START_CMD
                    .parse("path", server.path)
                    .parse("id", server.id)
                    .parse()
            );

            //TODO Send response to make the start button not clickable
            return Response.OK();
        }

        getServer(command.server).sendCommand(command.command);

        return Response.OK();
    }
}