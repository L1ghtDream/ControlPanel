package dev.lightdream.controlpanel.dto.data;

import dev.lightdream.common.dto.permission.PermissionEnum;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
public class PermissionData {

    public int userID;
    public HashMap<PermissionEnum, Boolean> permissions;

    @Override
    public String toString() {
        return "PermissionData{" +
                "userID=" + userID +
                ", permissions=" + permissions +
                '}';
    }
}
