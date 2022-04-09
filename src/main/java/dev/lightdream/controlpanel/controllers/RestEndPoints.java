package dev.lightdream.controlpanel.controllers;

import dev.lightdream.controlpanel.dto.Command;
import dev.lightdream.controlpanel.dto.Node;
import dev.lightdream.controlpanel.dto.Server;
import dev.lightdream.controlpanel.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import static dev.lightdream.controlpanel.utils.Utils.*;

@Slf4j
@Controller
public class RestEndPoints {

    public RestEndPoints() {

    }

    @MessageMapping("/server/api/server")
    public void send(Command command) {
        if(command.command.equals("start")){
            //todo safeguard for starting the server
            Server server = getServer(command.server);
            Node node = server.node;
            //todo
            node.sendCommand("cd /home/test; screen -dmS test -L -Logfile session.log bash -c \"sh start.sh\"; screen -S test -X colon \"logfile flush 0^M\"");
            return;
        }

        getServer(command.server).sendCommand(command.command);
    }
}