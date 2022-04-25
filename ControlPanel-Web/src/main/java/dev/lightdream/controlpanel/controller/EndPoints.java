package dev.lightdream.controlpanel.controller;

import dev.lightdream.common.database.Node;
import dev.lightdream.common.database.Server;
import dev.lightdream.common.database.User;
import dev.lightdream.common.dto.BuildProperties;
import dev.lightdream.common.dto.data.Cookie;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.dto.redis.RedisResponse;
import dev.lightdream.common.dto.redis.event.impl.GetBuildPropertiesEvent;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.controlpanel.Main;
import dev.lightdream.logger.Debugger;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SpringMVCViewInspection")
@Controller
public class EndPoints {

    @SuppressWarnings("unused")
    @GetMapping("/server/{serverName}")
    public String server(Model model, HttpServletRequest request, @PathVariable String serverName, @CookieValue(value = "login_data") String cookieBase64) {
        Cookie cookie = Utils.getCookie(cookieBase64);

        if (!cookie.validate()) {
            model.addAttribute("error", "401");
            return "error.html";
        }

        User user = cookie.getUser();
        Server server = Utils.getServer(serverName);

        if (!user.hasPermission(server, PermissionEnum.SERVER_VIEW)) {
            model.addAttribute("error", "401");
            return "error.html";
        }

        model.addAttribute("server", serverName);
        return "server.html";
    }

    @SuppressWarnings("unused")
    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        return "login.html";
    }

    @SuppressWarnings("unused")
    @GetMapping("/servers")
    public String servers(Model model, HttpServletRequest request, @CookieValue(value = "login_data") String cookieBase64) {
        Cookie cookie = Utils.getCookie(cookieBase64);

        if (!cookie.validate()) {
            model.addAttribute("error", "401");
            return "error.html";
        }

        User user = cookie.getUser();

        model.addAttribute("servers",
                Main.instance.getServers().stream().filter(server ->
                        user.hasPermission(server, PermissionEnum.SERVER_VIEW)).collect(Collectors.toList())
        );
        return "servers.html";
    }

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {
        Debugger.log("Cookie: '" + cookieBase64 + "'");
        Cookie cookie = Utils.getCookie(cookieBase64);

        if (!cookie.validate()) {
            return login(model, request);
        }

        return servers(model, request, cookieBase64);
    }

    // -------------------- ADMIN --------------------
    @SuppressWarnings("unused")
    @GetMapping("/admin/nodes")
    public String nodes(Model model, HttpServletRequest request, @CookieValue(value = "login_data") String cookieBase64) {
        Cookie cookie = Utils.getCookie(cookieBase64);

        if (!cookie.validate()) {
            model.addAttribute("error", "401");
            return "error.html";
        }

        model.addAttribute("nodes", Node.getNodes());

        return "admin/node/nodes.html";
    }

    @SuppressWarnings("unused")
    @GetMapping("/admin/node/{nodeID}")
    public String node(Model model, HttpServletRequest request, @CookieValue(value = "login_data") String cookieBase64, @PathVariable String nodeID) {
        Cookie cookie = Utils.getCookie(cookieBase64);
        Node node = Node.getNode(nodeID);

        model.addAttribute("node", node);

        return "admin/node/node.html";
    }

    @SuppressWarnings("unused")
    @GetMapping("/admin")
    public String admin(Model model, HttpServletRequest request, @CookieValue(value = "login_data") String cookieBase64) {
        Cookie cookie = Utils.getCookie(cookieBase64);

        if (!cookie.validate()) {
            model.addAttribute("error", "401");
            return "error.html";
        }

        List<NodeBuildProperties> nodeBuildProperties = new ArrayList<>();

        Node.getNodes().forEach(node -> {
            RedisResponse response = new GetBuildPropertiesEvent(node).sendAndWait();
            Debugger.log(response.hasTimeout());
            BuildProperties buildProperties = response.getResponse(BuildProperties.class);
            Debugger.log(buildProperties);
            nodeBuildProperties.add(new NodeBuildProperties(node, buildProperties));
        });

        model.addAttribute("build", Main.instance.buildProperties);
        model.addAttribute("nodes", nodeBuildProperties);

        return "admin/admin.html";
    }

    // -------------------- DEV --------------------

    @GetMapping("/dev/page_view")
    public String dev(String page) {
        return page;
    }

    // -------------------- Classes --------------------

    @AllArgsConstructor
    @NoArgsConstructor
    private static class NodeBuildProperties {
        public Node node;
        public BuildProperties build;
    }


}