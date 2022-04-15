package dev.lightdream.common.sftp.database;

import dev.lightdream.common.sftp.CommonMain;
import dev.lightdream.common.sftp.dto.permission.PermissionType;
import dev.lightdream.common.sftp.manager.PermissionHolder;
import dev.lightdream.common.sftp.utils.Utils;
import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.DatabaseEntry;

@DatabaseTable(table = "users")
public class User extends DatabaseEntry {

    @DatabaseField(columnName = "username")
    public String username;
    @DatabaseField(columnName = "password")
    public String password;
    @DatabaseField(columnName = "otp_secret")
    public String otpSecret;

    @SuppressWarnings("unused")
    public User() {
        super(CommonMain.instance);
    }

    public User(String username, String password, String otpSecret) {
        super(CommonMain.instance);
        this.username = username;
        this.password = password;
        this.otpSecret = otpSecret;
    }

    @SuppressWarnings("unused")
    public String generateQR() {
        String path = CommonMain.instance.qrPath() + username + ".png";
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
