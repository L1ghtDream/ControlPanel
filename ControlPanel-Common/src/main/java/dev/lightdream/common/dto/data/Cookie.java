package dev.lightdream.common.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.lightdream.common.database.User;
import dev.lightdream.common.utils.AuthUtils;
import dev.lightdream.common.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Cookie {

    public int id;
    public String username;
    public String hash;

    public boolean validate() {
        if (hash == null || hash.equals("")) {
            return false;
        }

        User user = getUser();

        if (user == null) {
            return false;
        }

        if (user.id != this.id) {
            return false;
        }

        return AuthUtils.checkHash(user, hash);
    }

    @JsonIgnore
    public User getUser() {
        return User.getUser(username);
    }

    @Override
    public String toString() {
        return Utils.toJson(this);
    }
}
