package com.deshko.task4.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordHasherTest {

    @Test
    void hash_shouldReturnSameHashForSamePassword() {
        String password = "myPassword123";

        String hash1 = PasswordHasher.hash(password);
        String hash2 = PasswordHasher.hash(password);

        assertEquals(hash1, hash2);
    }

    @Test
    void hash_shouldReturnDifferentHashForDifferentPasswords() {
        String hash1 = PasswordHasher.hash("password1");
        String hash2 = PasswordHasher.hash("password2");

        assertNotEquals(hash1, hash2);
    }

    @Test
    void hash_shouldReturnNonEmptyString() {
        String hash = PasswordHasher.hash("test");

        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }

    @Test
    void hash_shouldBeDeterministic() {
        String input = "securePassword";
        String expected = PasswordHasher.hash(input);

        for (int i = 0; i < 10; i++) {
            assertEquals(expected, PasswordHasher.hash(input));
        }
    }

    @Test
    void hash_shouldMatchKnownSha256Value() {
        String input = "abc";
        String expected = "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad";

        assertEquals(expected, PasswordHasher.hash(input));
    }

    @Test
    void hash_shouldHandleEmptyString() {
        String hash = PasswordHasher.hash("");

        assertNotNull(hash);
        assertEquals(64, hash.length());
    }

    @Test
    void hash_shouldAlwaysReturn64CharHexString() {
        String hash = PasswordHasher.hash("anything");

        assertEquals(64, hash.length());
        assertTrue(hash.matches("[0-9a-f]+"));
    }
}