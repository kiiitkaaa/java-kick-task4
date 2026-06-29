package com.deshko.task4.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {
    private final UserValidator validator = UserValidator.getInstance();

    @Test
    void shouldReturnFalseForNullPassword() {
        assertFalse(validator.isValidPassword(null));
    }

    @Test
    void shouldReturnFalseForBlankPassword() {
        assertFalse(validator.isValidPassword(" "));
    }

    @Test
    void shouldReturnFalseWhenNoDigits() {
        assertFalse(validator.isValidPassword("abcdef"));
    }

    @Test
    void shouldReturnFalseWhenNoLetters() {
        assertFalse(validator.isValidPassword("123456"));
    }

    @Test
    void shouldReturnFalseWhenTooShort() {
        assertFalse(validator.isValidPassword("a1b2"));
    }

    @Test
    void shouldReturnFalseWhenContainsSpecialCharacters() {
        assertFalse(validator.isValidPassword("abc123!"));
    }

    @Test
    void shouldReturnTrueForValidPassword() {
        assertTrue(validator.isValidPassword("abc123"));
    }

    @Test
    void shouldReturnSameInstance() {
        assertSame(
                UserValidator.getInstance(),
                UserValidator.getInstance()
        );
    }
}

