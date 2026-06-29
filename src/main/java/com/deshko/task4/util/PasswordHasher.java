package com.deshko.task4.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PasswordHasher {
    private static final Logger logger = LogManager.getLogger(PasswordHasher.class);
    private static final String ALGORITHM = "SHA-256";

    public static String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.fatal("Critical error: Algorithm '{}' not found for password hashing.",
                    ALGORITHM, e);
            throw new ExceptionInInitializerError(e);
        }
    }
}