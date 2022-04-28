package dev.lightdream.controlpanel.controller.end_points;

import dev.lightdream.controlpanel.controller.EndPoints;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class User extends EndPoints {

    public static User instance;

    public User() {
        if (instance == null) {
            instance = this;
        }
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {
        return executeEndPoint(model, request, cookieBase64,
                "user/profile.html",
                (user) -> {
                    model.addAttribute("user", user);
                    return null;
                }
        );
    }
}
