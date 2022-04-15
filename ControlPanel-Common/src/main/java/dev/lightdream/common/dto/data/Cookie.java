package dev.lightdream.common.dto.data;

import com.google.common.hash.Hashing;
import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.User;
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
        User user = CommonMain.instance.getDatabaseManager().getUser(username);

        Debugger.log(Hashing.sha256()
                .hashString(username + user.password + user.otpSecret, StandardCharsets.UTF_8));

        Debugger.log(hash);

        return Hashing.sha256()
                .hashString(username + user.password + user.otpSecret, StandardCharsets.UTF_8)
                .toString()
                .equals(hash);
    }

    public User getUser() {
        return CommonMain.instance.getDatabaseManager().getUser(username);
    }
}
