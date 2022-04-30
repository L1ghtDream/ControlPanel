package dev.lightdream.controlpanel.controller.rest_end_points;

import dev.lightdream.common.database.GlobalPermissionContainer;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.dto.response.Response;
import dev.lightdream.controlpanel.controller.RestEndPoints;
import dev.lightdream.common.dto.data.impl.NodeData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NodeRest extends RestEndPoints {

    public static NodeRest instance;

    public NodeRest() {
        if (instance == null) {
            instance = this;
        }
    }

    @PostMapping("/api/node/{nodeID}/save")
    @ResponseBody
    public Response nodeSettings(HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64,
                                 @PathVariable String nodeID, @RequestBody NodeData data) {

        dev.lightdream.common.database.Node node = dev.lightdream.common.database.Node.getNode(nodeID);

        return executeEndPoint(request, cookieBase64,
                (user) -> {
                    if (!data.validate()) {
                        return Response.BAD_DATA(data.getInvalidFields());
                    }

                    node.name = data.name;
                    node.ip = data.ip;
                    node.username = data.username;
                    node.sshPort = data.sshPort;
                    node.save();
                    return Response.OK();
                },
                GlobalPermissionContainer.getInstance(), PermissionEnum.GLOBAL_MANAGE_NODES
        );

    }

    @PostMapping("/api/node/{nodeID}/delete")
    @ResponseBody
    public Response nodeDelete(HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64,
                               @PathVariable String nodeID) {
        return executeEndPoint(request, cookieBase64,
                (user) -> {
                    dev.lightdream.common.database.Node node = dev.lightdream.common.database.Node.getNode(nodeID);

                    if (node == null) {
                        return Response.NOT_FOUND();
                    }

                    node.delete();
                    return Response.OK();
                },
                GlobalPermissionContainer.getInstance(), PermissionEnum.GLOBAL_MANAGE_NODES
        );
    }

}
