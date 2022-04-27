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
    public String server(Model model, HttpServletRequest request, @PathVariable String serverName, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {
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
        return "server/server.html";
    }

    @SuppressWarnings("unused")
    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        return "login.html";
    }

    @SuppressWarnings("unused")
    @GetMapping("/servers")
    public String servers(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {
        Cookie cookie = Utils.getCookie(cookieBase64);

        if (!cookie.validate()) {
            model.addAttribute("error", "401");
            return "error.html";
        }

        User user = cookie.getUser();

        model.addAttribute("servers",
                Server.getServers().stream().filter(server ->
                        user.hasPermission(server, PermissionEnum.SERVER_VIEW)).collect(Collectors.toList())
        );
        return "server/servers.html";
    }

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {
        Cookie cookie = Utils.getCookie(cookieBase64);

        if (!cookie.validate()) {
            return login(model, request);
        }

        return servers(model, request, cookieBase64);
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {
        Cookie cookie = Utils.getCookie(cookieBase64);

        if (!cookie.validate()) {
            return login(model, request);
        }

        model.addAttribute("user", cookie.getUser());

        return "user/profile.html";
    }

    // -------------------- ADMIN --------------------
    @SuppressWarnings("unused")
    @GetMapping("/admin/nodes")
    public String nodes(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {
        Cookie cookie = Utils.getCookie(cookieBase64);

        if (!cookie.validate()) {
            model.addAttribute("error", "401");
            return "error.html";
        }

        model.addAttribute("nodes", Node.getNodes());

        return "admin/node/nodes.html";
    }

    @SuppressWarnings("unused")
    @GetMapping("/admin/users")
    public String users(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {
        Cookie cookie = Utils.getCookie(cookieBase64);

        if (!cookie.validate()) {
            model.addAttribute("error", "401");
            return "error.html";
        }

        model.addAttribute("users", User.getUsers());

        return "admin/user/users.html";
    }

    @SuppressWarnings("unused")
    @GetMapping("/admin/node/{nodeID}")
    public String node(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64, @PathVariable String nodeID) {
        Cookie cookie = Utils.getCookie(cookieBase64);
        Node node = Node.getNode(nodeID);

        if (node == null) {
            model.addAttribute("error", "404");
            return "error.html";
        }

        model.addAttribute("node", node);

        return "admin/node/node.html";
    }

    @SuppressWarnings("unused")
    @GetMapping("/admin/user/{id}")
    public String user(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64, @PathVariable int id) {
        Cookie cookie = Utils.getCookie(cookieBase64);
        User user = User.getUser(id);

        if (user == null) {
            model.addAttribute("error", "404");
            return "error.html";
        }

        model.addAttribute("user", user);

        return "admin/user/user.html";
    }

    @SuppressWarnings("unused")
    @GetMapping("/admin")
    public String admin(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {
        Cookie cookie = Utils.getCookie(cookieBase64);

        if (!cookie.validate()) {
            model.addAttribute("error", "401");
            return "error.html";
        }

        List<NodeBuildProperties> nodeBuildProperties = new ArrayList<>();

        Node.getNodes().forEach(node -> {
            RedisResponse response = new GetBuildPropertiesEvent(node).sendAndWait();
            BuildProperties buildProperties = response.getResponse(BuildProperties.class);
            nodeBuildProperties.add(new NodeBuildProperties(node, buildProperties));
        });

        model.addAttribute("build", Main.instance.buildProperties);
        model.addAttribute("nodes", nodeBuildProperties);

        return "admin/admin.html";
    }

    @SuppressWarnings("unused")
    @GetMapping("/admin/node/create")
    public String createNode(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {
        Cookie cookie = Utils.getCookie(cookieBase64);

        if (!cookie.validate()) {
            model.addAttribute("error", "401");
            return "error.html";
        }

        return "admin/node/node_create.html";
    }

    @SuppressWarnings("unused")
    @GetMapping("/admin/user/create")
    public String createUser(Model model, HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64) {
        Cookie cookie = Utils.getCookie(cookieBase64);

        if (!cookie.validate()) {
            model.addAttribute("error", "401");
            return "error.html";
        }

        return "admin/user/user_create.html";
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