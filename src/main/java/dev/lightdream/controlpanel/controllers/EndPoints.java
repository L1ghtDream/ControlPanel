package dev.lightdream.controlpanel.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("SpringMVCViewInspection")
@Controller
public class EndPoints {

    @GetMapping("/server/{server}")
    public String index(Model model, @PathVariable String server, HttpServletRequest request) {
        model.addAttribute("server", server);
        return "server.html";
    }
}