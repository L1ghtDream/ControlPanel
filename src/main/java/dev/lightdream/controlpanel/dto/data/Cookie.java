package dev.lightdream.controlpanel.dto.data;

import com.google.common.hash.Hashing;
import dev.lightdream.controlpanel.Main;
import dev.lightdream.controlpanel.dto.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;

@AllArgsConstructor
@NoArgsConstructor
public class Cookie {

    public String username;
    public String hash;

    public boolean check() {
        User user = Main.instance.databaseManager.getUser(username);

        return Hashing.sha256()
                .hashString(username + user.password + user.otpSecret, StandardCharsets.UTF_8)
                .toString()
                .equals(hash);
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "username='" + username + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }
}
