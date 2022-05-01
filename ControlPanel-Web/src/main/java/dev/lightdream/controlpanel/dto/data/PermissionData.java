package dev.lightdream.controlpanel.dto.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
public class PermissionData {

    public int userID;
    public HashMap<String, Boolean> permissions;

}
