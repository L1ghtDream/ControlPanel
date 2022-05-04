package dev.lightdream.controlpanel.controller.end_points.admin;

import dev.lightdream.common.database.GlobalPermissionContainer;
import dev.lightdream.common.database.Node;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.controlpanel.controller.EndPoints;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ServerAdmin extends EndPoints {

    public static ServerAdmin instance;

    public ServerAdmin() {
        if (instance == null) {
            instance = this;
        }
    }

    @GetMapping("/admin/server/create")
    public String createServer(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {

        return executeEndPoint(model, request, cookieBase64,
                "server/create.html",
                user->{
                    model.addAttribute("nodes", Node.getNodes());
                    return null;
                },
                GlobalPermissionContainer.getInstance(), PermissionEnum.GLOBAL_MANAGE_USERS
        );
    }

}
