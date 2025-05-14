package com.example.encryption.util;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * Utility class to handle AES-GCM encryption and decryption using password-based key
 * derivation (PBKDF2).
 */
public class CryptoUtil {

    // Constants used in encryption
    private static final String CIPHER_ALGORITHM = "AES/GCM/NoPadding";
    private static final String KEY_ALGORITHM = "PBKDF2WithHmacSHA512";
    private static final int SALT_LENGTH = 16;
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH = 16;
    private static final int KEY_LENGTH = 32; // 256-bit AES
    private static final int ITERATIONS = 65536;

    /**
     * Encrypts the given plain text using the provided password.
     */
    public static String encrypt(String password, String plainText) throws Exception {
        if (password == null || plainText == null) {
            throw new IllegalArgumentException("Password and plaintext must not be null");
        }

        byte[] salt = generateRandomBytes(SALT_LENGTH);
        byte[] iv = generateRandomBytes(IV_LENGTH);
        SecretKey key = deriveKey(password, salt);

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH * 8, iv));
        byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // Combine salt + IV + encrypted data into one byte array
        ByteBuffer buffer = ByteBuffer.allocate(salt.length + iv.length + cipherText.length);
        buffer.put(salt);
        buffer.put(iv);
        buffer.put(cipherText);

        return Base64.getEncoder().encodeToString(buffer.array());
    }

    /**
     * Decrypts the given encrypted Base64 string using the provided password.
     */
    public static String decrypt(String encrypted, String password) throws Exception {
        if (encrypted == null || password == null) {
            throw new IllegalArgumentException("Encrypted data and password must not be null");
        }

        byte[] decoded = Base64.getDecoder().decode(encrypted);
        ByteBuffer buffer = ByteBuffer.wrap(decoded);

        // Extract salt, IV and ciphertext
        byte[] salt = new byte[SALT_LENGTH];
        buffer.get(salt);
        byte[] iv = new byte[IV_LENGTH];
        buffer.get(iv);
        byte[] cipherText = new byte[buffer.remaining()];
        buffer.get(cipherText);

        SecretKey key = deriveKey(password, salt);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH * 8, iv));

        byte[] plainText = cipher.doFinal(cipherText);
        return new String(plainText, StandardCharsets.UTF_8);
    }

    /**
     * Derives a 256-bit AES key using PBKDF2 with the provided password and salt.
     */
    private static SecretKey deriveKey(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH * 8);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    /**
     * Generates a secure random byte array of the given length.
     */
    private static byte[] generateRandomBytes(int length) {
        byte[] bytes = new byte[length];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }
}
