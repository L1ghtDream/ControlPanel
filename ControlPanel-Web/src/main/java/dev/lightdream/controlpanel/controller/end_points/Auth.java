package dev.lightdream.controlpanel.controller.end_points;

import dev.lightdream.common.dto.data.Cookie;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.controlpanel.controller.EndPoints;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("SpringMVCViewInspection")
@Controller
public class Auth extends EndPoints {

    public static Auth instance;

    public Auth() {
        if (instance == null) {
            instance = this;
        }
    }

    //Do not move to EndPoints#executeEndPoint()
    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {
        Cookie cookie = Utils.getCookie(cookieBase64);
        if (cookie.validate()) {
            return Server.instance.servers(model, request, cookieBase64);
        }

        return "login.html";
    }

}
