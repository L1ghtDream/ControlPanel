package dev.lightdream.controlpanel.controller;

import dev.lightdream.common.database.User;
import dev.lightdream.common.dto.data.Cookie;
import dev.lightdream.common.dto.permission.PermissionContainer;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.controlpanel.controller.end_points.Auth;
import dev.lightdream.lambda.LambdaExecutor;
import dev.lightdream.logger.Debugger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

@Controller
public abstract class EndPoints {

    /**
     * @param model               Spring boot model
     * @param request             Spring boot request
     * @param cookieBase64        login_data cookie
     * @param callback            Return null if you want to continue with the default behavior or return any template to render
     * @param permissionContainer Permission container to check permissions
     * @param permissions         Permissions to check
     * @param template            Template to render if permission by default behavior
     * @return Template to render
     */
    public static String executeEndPoint(Model model, HttpServletRequest request, String cookieBase64,
                                         String template,
                                         LambdaExecutor.ReturnLambdaExecutor<String, User> callback,
                                         PermissionContainer permissionContainer, PermissionEnum... permissions) {
        Debugger.log("Executing end point with template " + template);

        Cookie cookie = Utils.getCookie(cookieBase64);

        if (!cookie.validate()) {
            return Auth.instance.login(model, request, template);
        }

        User user = cookie.getUser();

        if (permissionContainer != null) {
            for (PermissionEnum permission : permissions) {
                if (!user.hasPermission(permissionContainer, permission)) {
                    model.addAttribute("error", "401");
                    return "error.html";
                }
            }
        }

        if (callback != null) {
            String output = callback.execute(user);
            if (output != null) {
                return output;
            }
        }

        return template;
    }

    @SuppressWarnings("ConfusingArgumentToVarargsMethod")
    public static String executeEndPoint(Model model, HttpServletRequest request, String cookieBase64,
                                         String template,
                                         LambdaExecutor.ReturnLambdaExecutor<String, User> callback) {
        return executeEndPoint(model, request, cookieBase64, template, callback, null, null);
    }

    @SuppressWarnings({"ConfusingArgumentToVarargsMethod", "unused"})
    public static String executeEndPoint(Model model, HttpServletRequest request, String cookieBase64, String template) {
        return executeEndPoint(model, request, cookieBase64, template, null, null, null);
    }

    public static String executeEndPoint(Model model, HttpServletRequest request, String cookieBase64,
                                         String template,
                                         PermissionContainer permissionContainer, PermissionEnum... permissions) {
        return executeEndPoint(model, request, cookieBase64, template, null, permissionContainer, permissions);
    }


    // -------------------- DEV --------------------

}