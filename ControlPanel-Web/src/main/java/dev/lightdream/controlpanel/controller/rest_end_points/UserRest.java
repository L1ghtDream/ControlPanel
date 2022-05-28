package dev.lightdream.controlpanel.controller.rest_end_points;

import dev.lightdream.common.database.GlobalPermissionContainer;
import dev.lightdream.common.database.User;
import dev.lightdream.common.dto.data.impl.UserData;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.dto.response.Response;
import dev.lightdream.controlpanel.Main;
import dev.lightdream.controlpanel.controller.RestEndPoints;
import dev.lightdream.controlpanel.dto.data.OTPData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
public class UserRest extends RestEndPoints {

    public static UserRest instance;

    public UserRest() {
        if (instance == null) {
            instance = this;
        }
    }

    @PostMapping("/api/user/{userID}/save")
    @ResponseBody
    public Response userSettings(HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64,
                                 @PathVariable int userID, @RequestBody UserData.UpdateData data) {
        return executeEndPoint(request, cookieBase64,
                (user) -> {
                    if (!data.validate()) {
                        return Response.BAD_DATA(data.getInvalidFields());
                    }

                    dev.lightdream.common.database.User usr = dev.lightdream.common.database.User.getUser(userID);

                    if (user.hasPermission(PermissionEnum.GLOBAL_MANAGE_USERS) || user.getID().equals(userID)) {
                        usr.username = "NoOneWillNeverHaveThisUsernameBecauseItWouldBeRidiculousToHaveSuchAUsername";

                        usr.updatePassword(data.password);
                        usr.save();

                        usr.username = data.username;
                        usr.save();
                    }

                    if (!user.hasPermission(PermissionEnum.GLOBAL_MANAGE_USERS)) {
                        return Response.UNAUTHORISED();
                    }

                    usr.setPermission(PermissionEnum.GLOBAL_ADMIN, data.GLOBAL_ADMIN);
                    usr.setPermission(PermissionEnum.GLOBAL_MANAGE_USERS, data.GLOBAL_MANAGE_USERS);
                    usr.setPermission(PermissionEnum.GLOBAL_MANAGE_NODES, data.GLOBAL_MANAGE_NODES);
                    usr.setPermission(PermissionEnum.GLOBAL_VIEW, data.GLOBAL_VIEW);

                    usr.save();

                    return Response.OK();
                });
    }

    @PostMapping("/api/user/create")
    @ResponseBody
    public Response userCreate(HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64,
                               @RequestBody UserData.CreateData data) {
        return executeEndPoint(request, cookieBase64,
                (user) -> {
                    if (!data.validate()) {
                        return Response.BAD_DATA(data.getInvalidFields());
                    }

                    dev.lightdream.common.database.User usr = new dev.lightdream.common.database.User(data.username, data.password);
                    usr.save();
                    usr = dev.lightdream.common.database.User.getUser(data.username);

                    usr.setPermission(PermissionEnum.GLOBAL_ADMIN, data.GLOBAL_ADMIN);
                    usr.setPermission(PermissionEnum.GLOBAL_MANAGE_USERS, data.GLOBAL_MANAGE_USERS);
                    usr.setPermission(PermissionEnum.GLOBAL_MANAGE_NODES, data.GLOBAL_MANAGE_NODES);
                    usr.setPermission(PermissionEnum.GLOBAL_VIEW, data.GLOBAL_VIEW);

                    return Response.OK();
                },
                GlobalPermissionContainer.getInstance(), PermissionEnum.GLOBAL_MANAGE_USERS
        );
    }

    @PostMapping("/api/user/{userID}/disable-2fa")
    @ResponseBody
    public Response userDisable2FA(HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64,
                                   @PathVariable Integer userID) {
        return executeEndPoint(request, cookieBase64,
                (user) -> {
                    dev.lightdream.common.database.User usr = Main.instance.databaseManager.getUser(userID);


                    if (usr == null) {
                        return Response.NOT_FOUND();
                    }

                    if (!Objects.equals(user.id, usr.id)) {
                        if (!user.hasPermission(PermissionEnum.GLOBAL_MANAGE_USERS)) {
                            return Response.UNAUTHORISED();
                        }
                    }

                    usr.disable2FA();
                    return Response.OK();
                },
                GlobalPermissionContainer.getInstance()
        );
    }

    @PostMapping("/api/user/{userID}/enable-2fa")
    @ResponseBody
    public Response enable2FA(HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64,
                              @PathVariable Integer userID, @RequestBody OTPData data) {
        return executeEndPoint(request, cookieBase64,
                (user) -> {
                    dev.lightdream.common.database.User usr = Main.instance.databaseManager.getUser(userID);

                    if (usr == null) {
                        return Response.NOT_FOUND();
                    }

                    if (!Objects.equals(user.id, usr.id)) {
                        if (!user.hasPermission(PermissionEnum.GLOBAL_MANAGE_USERS)) {
                            return Response.UNAUTHORISED();
                        }
                    }

                    if (!user.activate2FA(data.otp)) {
                        return Response.UNAUTHORISED();
                    }
                    return Response.OK();
                },
                GlobalPermissionContainer.getInstance()
        );
    }

    @PostMapping("/api/user/{userID}/delete")
    @ResponseBody
    public Response userDelete(HttpServletRequest request, @CookieValue(value = "login_data", defaultValue = "") String cookieBase64,
                               @PathVariable int userID) {
        return executeEndPoint(request, cookieBase64,
                (user) -> {
                    dev.lightdream.common.database.User usr = dev.lightdream.common.database.User.getUser(userID);

                    if (usr == null) {
                        return Response.NOT_FOUND();
                    }

                    usr.delete();

                    return Response.OK();
                },
                GlobalPermissionContainer.getInstance(), PermissionEnum.GLOBAL_MANAGE_USERS
        );
    }

    @SuppressWarnings("unused")
    @PostMapping("/api/user/get/id/{username}")
    @ResponseBody
    public Response getUserID(HttpServletRequest request, @PathVariable String username) {
        User user = User.getUser(username);
        if (user == null) {
            return Response.NOT_FOUND();
        }
        user.password = "[HIDDEN]";
        user.otpSecret = "[HIDDEN]";
        return Response.OK(user.id);
    }

}
