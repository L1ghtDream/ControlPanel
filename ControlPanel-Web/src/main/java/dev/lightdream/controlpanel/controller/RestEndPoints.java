package dev.lightdream.controlpanel.controller;

import dev.lightdream.common.database.User;
import dev.lightdream.common.dto.data.Cookie;
import dev.lightdream.common.dto.permission.PermissionContainer;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.dto.response.Response;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.lambda.LambdaExecutor;
import dev.lightdream.logger.Debugger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public abstract class RestEndPoints {

    /**
     * @param request             Spring boot request
     * @param cookieBase64        login_data cookie
     * @param callback            Return null if you want to continue with the default behavior or return any template to render
     * @param permissionContainer Permission container to check permissions
     * @param permissions         Permissions to check
     * @return Template to render
     */
    public static Response executeEndPoint(HttpServletRequest request, String cookieBase64,
                                           LambdaExecutor.ReturnLambdaExecutor<Response, User> callback,
                                           PermissionContainer permissionContainer, PermissionEnum... permissions) {
        if (request != null) {
            Debugger.log("Received rest request with url " + request.getRequestURI());
        } else {
            Debugger.log("Received rest request with url unknown");
        }

        Debugger.log("[Base64] Cookie: " + cookieBase64);
        Debugger.log("[Ascii ] Cookie: " + Utils.base64Decode(cookieBase64));
        Cookie cookie = Utils.getCookie(cookieBase64);

        if (!cookie.validate()) {
            return Response.UNAUTHORISED();
        }

        User user = cookie.getUser();

        if (permissionContainer != null) {

            for (PermissionEnum permission : permissions) {
                if (!user.hasPermission(permissionContainer, permission)) {
                    return Response.UNAUTHORISED();
                }
            }
        }

        if (callback != null) {
            Response output = callback.execute(user);
            if (output != null) {
                return output;
            }
        }

        return Response.OK();
    }

    public static Response executeEndPoint(HttpServletRequest request, String cookieBase64,
                                           PermissionContainer permissionContainer, PermissionEnum... permissions) {
        return executeEndPoint(request, cookieBase64, null, permissionContainer, permissions);
    }

    @SuppressWarnings("ConfusingArgumentToVarargsMethod")
    public static Response executeEndPoint(HttpServletRequest request, String cookieBase64,
                                           LambdaExecutor.ReturnLambdaExecutor<Response, User> callback) {
        return executeEndPoint(request, cookieBase64, callback, null, null);
    }

    @SuppressWarnings("ConfusingArgumentToVarargsMethod")
    public static Response executeEndPoint(HttpServletRequest request, String cookieBase64) {
        return executeEndPoint(request, cookieBase64, null, null, null);
    }

}
