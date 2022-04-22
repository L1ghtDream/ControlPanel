package dev.lightdream.node.controller;

import dev.lightdream.common.database.Node;
import dev.lightdream.common.database.Server;
import dev.lightdream.common.dto.Command;
import dev.lightdream.common.dto.response.Response;
import dev.lightdream.logger.Debugger;
import dev.lightdream.common.dto.ServerStats;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class RestEndPoints {

    public RestEndPoints() {

    }

    @PostMapping("/api/execute")
    @ResponseBody
    public Response executeCommand(@RequestBody Command command) {
        Node.executeCommandLocal(command.getCommand());
        return Response.OK();
    }

    @SneakyThrows
    @PostMapping("/api/stats/{serverName}")
    @ResponseBody
    public Response cpuUsage(@PathVariable String serverName) {
        Debugger.log("Received request for cpu usage");
        Thread.sleep(10000);
        Debugger.log("Sending cpu usage");
        Server server = Server.getServer(serverName);



        return Response.OK(new ServerStats(

        ));
    }

}