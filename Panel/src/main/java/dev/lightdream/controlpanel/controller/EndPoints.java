package dev.lightdream.controlpanel.controller;

import com.google.gson.Gson;
import dev.lightdream.common.sftp.database.Server;
import dev.lightdream.common.sftp.database.User;
import dev.lightdream.common.sftp.dto.data.Cookie;
import dev.lightdream.common.sftp.dto.permission.PermissionType;
import dev.lightdream.common.sftp.utils.Utils;
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
    @GetMapping("/server/{serverName}")
    public String server(Model model, HttpServletRequest request, @PathVariable String serverName, @CookieValue(value = "login_data") String cookieBase64) {
        Cookie cookie = getCookie(cookieBase64);

        if (!cookie.validate()) {
            model.addAttribute("error", "401");
            return "error.html";
        }

        User user = cookie.getUser();
        Server server = Utils.getServer(serverName);

        if (!user.hasPermission(server, PermissionType.SERVER_VIEW)) {
            model.addAttribute("error", "401");
            return "error.html";
        }

        model.addAttribute("server", serverName);
        return "server.html";
    }

    @SuppressWarnings("unused")
    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        return "login.html";
    }

    public Cookie getCookie(String cookie) {
        return new Gson().fromJson(Utils.base64Decode(cookie), Cookie.class);
    }
}