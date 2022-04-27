package dev.lightdream.controlpanel.controller.end_points;

import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.controlpanel.controller.EndPoints;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@Controller
public class Server extends EndPoints {

    public static Server instance;

    public Server() {
        if (instance == null) {
            instance = this;
        }
    }

    @GetMapping("/server/{serverID}")
    public String server(Model model, HttpServletRequest request, @PathVariable String serverID, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {
        dev.lightdream.common.database.Server server = Utils.getServer(serverID);

        return executeEndPoint(model, request, cookieBase64,
                "server/server.html",
                (user) -> {
                    model.addAttribute("server", serverID); // TODO move to server object
                    return null;
                },
                server, PermissionEnum.SERVER_VIEW
        );
    }

    @GetMapping("/servers")
    public String servers(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {
        return executeEndPoint(model, request, cookieBase64,
                "servers/servers.html",
                (user) -> {
                    model.addAttribute("servers", dev.lightdream.common.database.Server.getServers().stream().filter(server -> {
                        //noinspection CodeBlock2Expr
                        return user.hasPermission(server, PermissionEnum.SERVER_VIEW);
                    }).collect(Collectors.toList()));
                    return null;
                }
        );
    }


}
