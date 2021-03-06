package dev.lightdream.common.dto.data;


import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.User;
import dev.lightdream.common.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class LoginData {

    public String username;
    public String password;
    public String otp;

    public boolean checkPassword() {
        User user = CommonMain.instance.databaseManager.getUser(username);
        if (user == null) {
            return false;
        }

        if (user.has2FA()) {
            return user.password.equals(password) &&
                    otp.equals(Utils.getTOTPCode(user.otpSecret));
        } else {
            return user.password.equals(password);
        }
    }

    public Cookie generateCookie() {
        if (!checkPassword()) {
            return null;
        }

        User user = CommonMain.instance.databaseManager.getUser(username);

        return new Cookie(
                user.id,
                user.username,
                user.generateHash()
        );
    }

    @SuppressWarnings("unused")
    public void getUser() {
        CommonMain.instance.databaseManager.getUser(username);
    }
}


