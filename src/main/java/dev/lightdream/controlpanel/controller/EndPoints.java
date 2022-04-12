package dev.lightdream.controlpanel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("SpringMVCViewInspection")
@Controller
public class EndPoints {

    @SuppressWarnings("unused")
    @GetMapping("/server/{server}")
    public String index(Model model, HttpServletRequest request, @PathVariable String server) {
        model.addAttribute("server", server);
        return "server.html";
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        return "login.html";
    }
}