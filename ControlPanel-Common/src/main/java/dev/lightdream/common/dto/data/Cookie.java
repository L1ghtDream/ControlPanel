package dev.lightdream.common.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.hash.Hashing;
import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;

@AllArgsConstructor
@NoArgsConstructor
public class Cookie {

    public String username;
    public String hash;

    public boolean validate() {
        User user = getUser();

        System.out.println("Cookie: " + username + ":" + hash);
        System.out.println("User: " + user.username + ":" + user.password + ":" + user.otpSecret);

        return Hashing.sha256()
                .hashString(username + user.password + user.otpSecret, StandardCharsets.UTF_8)
                .toString()
                .equals(hash);
    }

    @JsonIgnore
    public User getUser() {
        return CommonMain.instance.getDatabaseManager().getUser(username);
    }
}
