package dev.lightdream.common.database;

import dev.lightdream.common.dto.permission.PermissionContainer;
import dev.lightdream.common.dto.permission.PermissionEnum;

public class GlobalPermissionContainer extends PermissionContainer {

    private static GlobalPermissionContainer instance;

    public static GlobalPermissionContainer getInstance() {
        if (instance == null) {
            instance = new GlobalPermissionContainer();
        }
        return instance;
    }

    @Override
    public PermissionEnum.Type getType() {
        return PermissionEnum.Type.GLOBAL;
    }

    @Override
    public String getID() {
        return "GlobalPermissionContainer";
    }
}
