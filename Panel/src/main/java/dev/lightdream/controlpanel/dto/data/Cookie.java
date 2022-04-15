package dev.lightdream.controlpanel.dto.data;

import com.google.common.hash.Hashing;
import dev.lightdream.controlpanel.Main;
import dev.lightdream.controlpanel.dto.User;
import dev.lightdream.logger.Debugger;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;

@AllArgsConstructor
@NoArgsConstructor
public class Cookie {

    public String username;
    public String hash;

    public boolean validate() {
        User user = Main.instance.databaseManager.getUser(username);

        Debugger.log(Hashing.sha256()
                .hashString(username + user.password + user.otpSecret, StandardCharsets.UTF_8));

        Debugger.log(hash);

        return Hashing.sha256()
                .hashString(username + user.password + user.otpSecret, StandardCharsets.UTF_8)
                .toString()
                .equals(hash);
    }

    public User getUser() {
        return Main.instance.databaseManager.getUser(username);
    }
}
