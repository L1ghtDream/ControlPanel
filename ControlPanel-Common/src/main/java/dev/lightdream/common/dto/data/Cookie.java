package dev.lightdream.common.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.User;
import dev.lightdream.common.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Cookie {

    public int id;
    public String hash;

    public boolean validate() {
        if (hash == null || hash.equals("")) {
            return false;
        }

        User user = getUser();

        return user.generateHash().equals(hash);
    }

    @JsonIgnore
    public User getUser() {
        return CommonMain.instance.getDatabaseManager().getUser(id);
    }

    @Override
    public String toString() {
        return Utils.toJson(this);
    }
}
