package dev.lightdream.controlpanel.dto.data;


import com.google.common.hash.Hashing;
import dev.lightdream.common.database.User;
import dev.lightdream.common.dto.data.Cookie;
import dev.lightdream.controlpanel.Main;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;

@AllArgsConstructor
@NoArgsConstructor
public class LoginData {

    public String username;
    public String password;
    public String otp;

    @SuppressWarnings("unused")
    public boolean checkPassword() {
        User user = Main.instance.databaseManager.getUser(username);
        return true; // TODO Move to actual check
        //return user.password.equals(password) &&
        //        otp.equals(Utils.getTOTPCode(user.otpSecret));
    }

    public Cookie generateCookie() {
        if (!checkPassword()) {
            return null;
        }

        User user = Main.instance.databaseManager.getUser(username);

        return new Cookie(
                username,
                Hashing.sha256()
                        .hashString(username + user.password + user.otpSecret, StandardCharsets.UTF_8)
                        .toString()
        );
    }

    @SuppressWarnings("unused")
    public void getUser() {
        Main.instance.databaseManager.getUser(username);
    }
}


