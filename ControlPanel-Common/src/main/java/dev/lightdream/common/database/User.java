package dev.lightdream.common.database;

import com.google.common.hash.Hashing;
import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.permission.PermissionContainer;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.entry.impl.IntegerDatabaseEntry;

import java.nio.charset.StandardCharsets;
import java.util.List;

@DatabaseTable(table = "users")
public class User extends IntegerDatabaseEntry {

    @DatabaseField(columnName = "username", unique = true)
    public String username;
    @DatabaseField(columnName = "password")
    public String password;
    @DatabaseField(columnName = "otp_secret")
    public String otpSecret;

    @SuppressWarnings("unused")
    public User() {
        super(CommonMain.instance);
    }

    @SuppressWarnings("unused")
    public User(String username, String password, String otpSecret) {
        super(CommonMain.instance);
        this.username = username;
        this.password = password;
        this.otpSecret = otpSecret;
    }

    public static List<User> getUsers() {
        return CommonMain.instance.databaseManager.getAll(User.class);
    }

    @SuppressWarnings("unused")
    public String generateQR(String secret) {
        String path = CommonMain.instance.qrPath() + username + ".png";
        Utils.createQRCode(Utils.getGoogleAuthenticatorBarCode(secret, "admin", "Original.gg"), path);
        return path;
    }

    public boolean hasPermission(PermissionContainer PermissionContainer, PermissionEnum permission) {
        if (PermissionContainer == null) {
            return false;
        }
        return PermissionContainer.hasPermission(this, permission);
    }

    @SuppressWarnings("unused")
    public void addPermission(PermissionContainer PermissionContainer, PermissionEnum permission) {
        if (PermissionContainer == null) {
            return;
        }
        PermissionContainer.addPermission(this, permission);
    }

    public boolean has2FA() {
        return this.otpSecret != null;
    }

    public void setup2FA(String secret) {
        this.otpSecret = secret;
    }

    public String generateHash() {
        if (has2FA()) {
            return Hashing.sha256()
                    .hashString(username + password + otpSecret, StandardCharsets.UTF_8)
                    .toString();
        } else {
            return Hashing.sha256()
                    .hashString(username + password, StandardCharsets.UTF_8)
                    .toString();
        }

    }

    public List<Server> getServers() {
        return CommonMain.instance.databaseManager.getServers();
    }

}
