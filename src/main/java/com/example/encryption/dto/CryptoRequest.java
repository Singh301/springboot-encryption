package com.example.encryption.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for both encryption and decryption APIs.
 */
public class CryptoRequest {

    @NotBlank(message = "Key must not be blank")
    private String key;

    @NotBlank(message = "Data must not be blank")
    private String data;

    public CryptoRequest() {
    }

    public CryptoRequest(String key, String data) {
        this.key = key;
        this.data = data;
    }

    // Getters and Setters

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
