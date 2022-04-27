package dev.lightdream.controlpanel.controller.end_points;

import dev.lightdream.controlpanel.controller.EndPoints;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class Root extends EndPoints {

    public static Root instance;

    public Root() {
        if (instance == null) {
            instance = this;
        }
    }

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {
        return executeEndPoint(model, request, cookieBase64,
                null,
                (user) -> {
                    //noinspection CodeBlock2Expr
                    return Server.instance.servers(model, request, cookieBase64
                    );
                }
        );
    }

}
