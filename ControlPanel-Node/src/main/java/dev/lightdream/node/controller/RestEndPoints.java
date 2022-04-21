package dev.lightdream.node.controller;

import dev.lightdream.common.database.Node;
import dev.lightdream.common.dto.Command;
import dev.lightdream.common.dto.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
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

}