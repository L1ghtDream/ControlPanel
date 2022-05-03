package dev.lightdream.controlpanel.controller.rest_end_points;

import com.google.common.hash.Hashing;
import dev.lightdream.common.dto.data.Cookie;
import dev.lightdream.common.dto.data.LoginData;
import dev.lightdream.common.dto.response.Response;
import dev.lightdream.controlpanel.controller.RestEndPoints;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@Controller
public class AuthRest extends RestEndPoints {

    public static AuthRest instance;

    public AuthRest() {
        if (instance == null) {
            instance = this;
        }
    }

    @SuppressWarnings("unused")
    @PostMapping("/api/login")
    @ResponseBody
    public Response login(HttpServletRequest request,@RequestBody LoginData loginData) {
        loginData.password = Hashing.sha256()
                .hashString(loginData.password, StandardCharsets.UTF_8)
                .toString();
        Cookie cookie = loginData.generateCookie();

        if (cookie == null) {
            return Response.UNAUTHORISED("Invalid username or password");
        }

        return Response.OK(cookie);
    }

    @PostMapping("/api/login/cookie")
    @ResponseBody
    public Response loginCookie(HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {
        return executeEndPoint(request, cookieBase64);
    }
}
