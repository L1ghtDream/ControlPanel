package dev.lightdream.common.dto.data.impl;

import dev.lightdream.common.database.User;
import dev.lightdream.common.dto.data.Validatable;
import dev.lightdream.common.dto.data.annotation.Validate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UserData extends Validatable {

    @Validate(validateMethod = "validateID")
    public int id;
    @Validate(validateMethod = "validateUsername")
    public String username;

    public boolean GLOBAL_ADMIN;
    public boolean GLOBAL_MANAGE_USERS;
    public boolean GLOBAL_MANAGE_NODES;
    public boolean GLOBAL_VIEW;
    public boolean GLOBAL_CREATE_SERVER;

    @SuppressWarnings("unused")
    public boolean validateUsername() {
        User user = User.getUser(username);

        if (user == null) {
            return true;
        }

        return user.getID()== id;
    }

    public boolean validatePassword(String password) {
        return password.length() > 8 &&
                password.matches(".*[@$!%*#?&].*") &&
                password.matches(".*[0-9].*") &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                !password.contains(" ");
    }

    public boolean validateID() {
        return id == 0 || User.getUser(id) != null;
    }

    public static class CreateData extends UserData {

        @Validate(validateMethod = "validatePassword")
        public String password;

        /**
         * Password must contain at least one number 0-9
         * Password must contain at least one lowercase letter a-z
         * Password must contain at least one uppercase letter A-Z
         * Password must contain at least one special character @$!%*#?&
         *
         * @return true if password is valid
         */
        @SuppressWarnings("unused")
        public boolean validatePassword() {
            return validatePassword(password);
        }
    }

    public static class UpdateData extends UserData {

        @Validate(validateMethod = "validatePassword", emptyAllowed = true)
        public String password;

        /**
         * Pasword can be null or epmty to signify no change
         * <p>
         * Password must contain at least one number 0-9
         * Password must contain at least one lowercase letter a-z
         * Password must contain at least one uppercase letter A-Z
         * Password must contain at least one special character @$!%*#?&
         *
         * @return true if password is valid
         */
        @SuppressWarnings("unused")
        public boolean validatePassword() {
            if (password.equals("")) {
                return true;
            }
            return validatePassword(password);
        }
    }


}
