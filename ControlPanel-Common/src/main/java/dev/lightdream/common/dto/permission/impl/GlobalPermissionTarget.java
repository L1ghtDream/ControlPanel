package dev.lightdream.common.dto.permission.impl;

import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.dto.permission.PermissionTarget;

public class GlobalPermissionTarget extends PermissionTarget {

    public static GlobalPermissionTarget instance;

    public static GlobalPermissionTarget getInstance() {
        if (instance == null) {
            instance = new GlobalPermissionTarget();
        }
        return instance;
    }

    @Override
    public PermissionEnum.PermissionType getType() {
        return PermissionEnum.PermissionType.GLOBAL;
    }
}
