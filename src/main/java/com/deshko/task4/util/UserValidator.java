package com.deshko.task4.util;

public class UserValidator {
    private static final UserValidator INSTANCE = new UserValidator();
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$";

    private UserValidator() {}

    public static UserValidator getInstance() {
        return INSTANCE;
    }

    public boolean isValidPassword(String password) {
        if (password == null || password.isBlank()) {
            return false;
        }
        return password.matches(PASSWORD_REGEX);
    }
}
