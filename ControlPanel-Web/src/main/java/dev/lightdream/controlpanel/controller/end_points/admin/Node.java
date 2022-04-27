package dev.lightdream.controlpanel.controller.end_points.admin;

import dev.lightdream.common.database.GlobalPermissionContainer;
import dev.lightdream.common.dto.BuildProperties;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.controlpanel.controller.EndPoints;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class Node extends EndPoints {

    public static Node instance;

    public Node() {
        if (instance == null) {
            instance = this;
        }
    }


    @GetMapping("/admin/nodes")
    public String nodes(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {
        return executeEndPoint(model, request, cookieBase64,
                "admin/node/nodes.html",
                (user) -> {
                    model.addAttribute("nodes", dev.lightdream.common.database.Node.getNodes());
                    return null;
                },
                GlobalPermissionContainer.getInstance(), PermissionEnum.GLOBAL_MANAGE_NODES
        );
    }

    @GetMapping("/admin/node/{nodeID}")
    public String node(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64, @PathVariable String nodeID) {
        return executeEndPoint(model, request, cookieBase64,
                "admin/node/node.html",
                (user) -> {
                    dev.lightdream.common.database.Node node = dev.lightdream.common.database.Node.getNode(nodeID);

                    if (node == null) {
                        model.addAttribute("error", "404");
                        return "error.html";
                    }
                    model.addAttribute("node", node);
                    return null;
                },
                GlobalPermissionContainer.getInstance(), PermissionEnum.GLOBAL_MANAGE_NODES
        );
    }

    @GetMapping("/admin/node/create")
    public String createNode(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {

        return executeEndPoint(model, request, cookieBase64,
                "admin/node/create.html",
                GlobalPermissionContainer.getInstance(), PermissionEnum.GLOBAL_MANAGE_NODES
        );
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class NodeBuildProperties {
        public dev.lightdream.common.database.Node node;
        public BuildProperties build;
    }

}
