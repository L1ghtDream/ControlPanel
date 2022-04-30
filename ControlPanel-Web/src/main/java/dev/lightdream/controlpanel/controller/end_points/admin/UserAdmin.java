package dev.lightdream.controlpanel.controller.end_points.admin;

import dev.lightdream.common.database.GlobalPermissionContainer;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.controlpanel.controller.EndPoints;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserAdmin extends EndPoints {

    public static UserAdmin instance;

    public UserAdmin() {
        if (instance == null) {
            instance = this;
        }
    }


    @GetMapping("/admin/users")
    public String users(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {
        return executeEndPoint(model, request, cookieBase64,
                "user/users.html",
                (user) -> {
                    model.addAttribute("users", dev.lightdream.common.database.User.getUsers());
                    return null;
                },
                GlobalPermissionContainer.getInstance(), PermissionEnum.GLOBAL_MANAGE_USERS
        );
    }

    @GetMapping("/admin/user/{id}")
    public String user(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64, @PathVariable int id) {
        return executeEndPoint(model, request, cookieBase64,
                "user/user.html",
                (user) -> {
                    dev.lightdream.common.database.User usr = dev.lightdream.common.database.User.getUser(id);

                    if (usr == null) {
                        model.addAttribute("error", "404");
                        return "error.html";
                    }
                    model.addAttribute("user", usr);
                    return null;
                },
                GlobalPermissionContainer.getInstance(), PermissionEnum.GLOBAL_MANAGE_USERS
        );
    }

    @GetMapping("/admin/user/create")
    public String createUser(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {

        return executeEndPoint(model, request, cookieBase64,
                "user/create.html",
                null,
                GlobalPermissionContainer.getInstance(), PermissionEnum.GLOBAL_MANAGE_USERS
        );
    }

}
