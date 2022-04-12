package dev.lightdream.controlpanel.dto.data;


import com.google.common.hash.Hashing;
import dev.lightdream.controlpanel.Main;
import dev.lightdream.controlpanel.dto.User;
import dev.lightdream.controlpanel.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;

@AllArgsConstructor
@NoArgsConstructor
public class LoginData {

    public String username;
    public String password;
    public String otp;

    public boolean checkPassword() {
        User user = Main.instance.databaseManager.getUser(username);
        return user.password.equals(password) &&
                otp.equals(Utils.getTOTPCode(user.otpSecret));
    }

    public Cookie generateCookie() {
        if (!checkPassword()) {
            return null;
        }

        User user = Main.instance.databaseManager.getUser(username);

        return new Cookie(
                username,
                Hashing.sha256()
                        .hashString(username + password + user.otpSecret, StandardCharsets.UTF_8)
                        .toString()
        );
    }
}


