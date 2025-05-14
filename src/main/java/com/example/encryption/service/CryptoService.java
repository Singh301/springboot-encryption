package com.example.encryption.service;

import com.example.encryption.util.CryptoUtil;
import com.example.encryption.exception.DecryptionException;
import com.example.encryption.exception.EncryptionException;
import org.springframework.stereotype.Service;

/**
 * Service layer for encryption and decryption logic.
 * Delegates to CryptoUtil and handles exceptions.
 */
@Service
public class CryptoService {

    public String encrypt(String key, String data) {
        try {
            return CryptoUtil.encrypt(key, data);
        } catch (Exception e) {
            throw new EncryptionException("Encryption failed", e);
        }
    }

    public String decrypt(String key, String encryptedData) {
        try {
            return CryptoUtil.decrypt(encryptedData, key);
        } catch (Exception e) {
            throw new DecryptionException("Decryption failed", e);
        }
    }
}
