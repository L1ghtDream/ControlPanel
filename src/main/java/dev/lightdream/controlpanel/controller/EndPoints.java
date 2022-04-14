package dev.lightdream.controlpanel.controller;

import dev.lightdream.controlpanel.dto.request.ServerRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("SpringMVCViewInspection")
@Controller
public class EndPoints {

    @SuppressWarnings("unused")
    @GetMapping("/server/{server}")
    public String server(Model model, HttpServletRequest request, @PathVariable String server, @RequestBody ServerRequest serverRequest) {
        if (!serverRequest.cookie.check()) {
            model.addAttribute("error", "401");
            return "error.html";
        }

        model.addAttribute("server", server);
        return "server.html";
    }

    @SuppressWarnings("unused")
    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        return "login.html";
    }
}