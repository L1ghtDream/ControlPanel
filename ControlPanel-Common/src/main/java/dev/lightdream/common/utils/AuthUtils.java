package dev.lightdream.common.utils;

import dev.lightdream.common.database.User;

public class AuthUtils {

    @SuppressWarnings("unused")
    public static boolean checkHash(String username, String hash) {
        return checkHash(User.getUser(username), hash);
    }

    public static boolean checkHash(int id, String hash) {
        return checkHash(User.getUser(id), hash);
    }

    public static boolean checkHash(User user, String hash) {
        return user.generateHash().equals(hash);
    }

}
