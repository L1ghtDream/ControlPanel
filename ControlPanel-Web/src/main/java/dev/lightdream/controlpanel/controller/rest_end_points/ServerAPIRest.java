package dev.lightdream.controlpanel.controller.rest_end_points;

import dev.lightdream.common.database.Server;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.dto.response.Response;
import dev.lightdream.controlpanel.Main;
import dev.lightdream.controlpanel.controller.RestEndPoints;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ServerAPIRest extends RestEndPoints {

    public static ServerAPIRest instance;

    public ServerAPIRest() {
        if (instance == null) {
            instance = this;
        }
    }

    @SneakyThrows
    @PostMapping("/api/stats/{serverID}")
    @ResponseBody
    public Response cpuUsage(HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64,
                             @PathVariable String serverID) {
        Server server = Server.getServer(serverID);

        return executeEndPoint(request, cookieBase64,
                (user) -> {
                    //noinspection CodeBlock2Expr
                    return Response.OK(Main.instance.serversCache.getStats(server));
                },
                server, PermissionEnum.SERVER_VIEW
        );
    }


}
