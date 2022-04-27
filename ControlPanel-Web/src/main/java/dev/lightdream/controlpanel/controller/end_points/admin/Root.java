package dev.lightdream.controlpanel.controller.end_points.admin;

import dev.lightdream.common.database.GlobalPermissionContainer;
import dev.lightdream.common.database.Node;
import dev.lightdream.common.dto.BuildProperties;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.dto.redis.RedisResponse;
import dev.lightdream.common.dto.redis.event.impl.GetBuildPropertiesEvent;
import dev.lightdream.controlpanel.Main;
import dev.lightdream.controlpanel.controller.EndPoints;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class Root extends EndPoints {

    public static Root instance;

    public Root() {
        if (instance == null) {
            instance = this;
        }
    }


    @GetMapping("/admin")
    public String admin(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {
        return executeEndPoint(model, request, cookieBase64,
                "admin/admin.html",
                (user) -> {
                    List<NodeBuildProperties> nodeBuildProperties = new ArrayList<>();

                    Node.getNodes().forEach(node -> {
                        RedisResponse response = new GetBuildPropertiesEvent(node).sendAndWait();
                        BuildProperties buildProperties = response.getResponse(BuildProperties.class);
                        nodeBuildProperties.add(new NodeBuildProperties(node, buildProperties));
                    });

                    model.addAttribute("build", Main.instance.buildProperties);
                    model.addAttribute("nodes", nodeBuildProperties);
                    return null;
                },
                GlobalPermissionContainer.getInstance(), PermissionEnum.GLOBAL_GLOBAL_VIEW
        );

    }

}
