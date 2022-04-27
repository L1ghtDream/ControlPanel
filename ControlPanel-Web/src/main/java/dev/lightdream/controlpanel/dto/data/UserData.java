package dev.lightdream.controlpanel.dto.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UserData {

    public String username;
    public String password;

    public boolean GLOBAL_ADMIN;
    public boolean GLOBAL_MANAGE_USERS;
    public boolean GLOBAL_MANAGE_NODES;

}
