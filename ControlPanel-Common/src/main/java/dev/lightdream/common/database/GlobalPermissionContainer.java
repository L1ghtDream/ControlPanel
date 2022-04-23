package dev.lightdream.common.database;

import dev.lightdream.common.dto.permission.PermissionContainer;
import dev.lightdream.common.dto.permission.PermissionEnum;

public class GlobalPermissionContainer extends PermissionContainer {

    public static GlobalPermissionContainer instance;

    public static GlobalPermissionContainer getInstance() {
        if (instance == null) {
            instance = new GlobalPermissionContainer();
        }
        return instance;
    }

    @Override
    public PermissionEnum.PermissionType getType() {
        return PermissionEnum.PermissionType.GLOBAL;
    }

}
