package dev.lightdream.controlpanel.dto;

import dev.lightdream.controlpanel.Main;
import dev.lightdream.controlpanel.utils.Utils;
import dev.lightdream.controlpanel.database.PermissionHolder;
import dev.lightdream.controlpanel.dto.permission.PermissionType;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.DatabaseEntry;

@DatabaseTable(table = "users")
public class User extends DatabaseEntry {

    public String username;
    public String password;
    public String otpSecret;

    @SuppressWarnings("unused")
    public User() {
        super(Main.instance);
    }

    public User(String username, String password, String otpSecret) {
        super(Main.instance);
        this.username = username;
        this.password = password;
        this.otpSecret = otpSecret;
    }

    @SuppressWarnings("unused")
    public String generateQR() {
        String path = Main.instance.qrPath() + username + ".png";
        Utils.createQRCode(Utils.getGoogleAuthenticatorBarCode(this.otpSecret, "admin", "Original.gg"), path);
        return path;
    }

    public boolean hasPermission(PermissionHolder permissionHolder, PermissionType permission) {
        return permissionHolder.hasPermission(this, permission);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", otpSecret='" + otpSecret + '\'' +
                ", id=" + id +
                '}';
    }
}
