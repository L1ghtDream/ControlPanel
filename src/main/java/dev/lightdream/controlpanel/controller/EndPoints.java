package dev.lightdream.controlpanel.controller;

import com.google.gson.Gson;
import dev.lightdream.controlpanel.dto.data.LoginData;
import dev.lightdream.controlpanel.utils.Utils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("SpringMVCViewInspection")
@Controller
public class EndPoints {

    @SuppressWarnings("unused")
    @GetMapping("/server/{server}")
    public String server(Model model, HttpServletRequest request, @PathVariable String server, @CookieValue(value = "login_data") String cookie) {
        LoginData loginData = getLoginData(cookie);

        model.addAttribute("server", server);
        return "server.html";
    }

    @SuppressWarnings("unused")
    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        return "login.html";
    }

    public LoginData getLoginData(String cookie) {
        return new Gson().fromJson(Utils.base64Decode(cookie), LoginData.class);
    }
}