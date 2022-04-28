package dev.lightdream.common.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.hash.Hashing;
import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.permission.PermissionContainer;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.entry.impl.IntegerDatabaseEntry;
import dev.lightdream.logger.Debugger;

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

    public User(String username, String password) {
        super(CommonMain.instance);
        this.username = username;
        this.password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        this.otpSecret = null;
    }

    @JsonIgnore
    public static User getUser(String username) {
        return CommonMain.instance.databaseManager.getUser(username);
    }

    @JsonIgnore
    public static User getUser(int id) {
        return CommonMain.instance.databaseManager.getUser(id);
    }

    @JsonIgnore
    public static List<User> getUsers() {
        return CommonMain.instance.databaseManager.getAll(User.class);
    }

    @SuppressWarnings("unused")
    public String generateQR(String secret) {
        String path = CommonMain.instance.getConfig().qrURL + username + ".png";
        Utils.createQRCode(Utils.getGoogleAuthenticatorBarCode(secret, "admin", "Original.gg"),
                CommonMain.instance.getConfig().qrStorageLocation + username + ".png");
        return path;
    }

    public boolean hasPermission(PermissionContainer permissionContainer, PermissionEnum permission) {
        if (permissionContainer == null) {
            return false;
        }
        return permissionContainer.hasPermission(this, permission);
    }

    @SuppressWarnings("unused")
    public boolean hasPermission(PermissionEnum permission) {
        return hasPermission(GlobalPermissionContainer.getInstance(), permission);
    }

    @SuppressWarnings("unused")
    public boolean hasPermission(String permission) {
        Debugger.log("1");
        return hasPermission(GlobalPermissionContainer.getInstance(), PermissionEnum.valueOf(permission));
    }

    @SuppressWarnings("unused")
    public void addPermission(PermissionContainer permissionContainer, PermissionEnum permission) {
        if (permissionContainer == null) {
            return;
        }
        permissionContainer.addPermission(this, permission);
    }

    public void setPermission(PermissionContainer permissionContainer, PermissionEnum permission, boolean value) {
        if (permissionContainer == null) {
            return;
        }
        permissionContainer.setPermission(this, permission, value);
    }

    public void setPermission(PermissionEnum permission, boolean value) {
        GlobalPermissionContainer.getInstance().setPermission(this, permission, value);
    }


    public boolean has2FA() {
        return this.otpSecret != null;
    }

    @SuppressWarnings("unused")
    public void setup2FA(String secret) {
        this.otpSecret = secret;
        save();
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

    @JsonIgnore
    public List<Server> getServers() {
        return CommonMain.instance.databaseManager.getServers();
    }

    public void updatePassword(String password) {
        this.password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        save();
    }

    public void disable2FA() {
        this.otpSecret = null;
        save();
    }

    public boolean checkRawPassword(String rawPassword) {
        return Hashing.sha256()
                .hashString(rawPassword, StandardCharsets.UTF_8)
                .toString().equals(this.password);
    }


}
