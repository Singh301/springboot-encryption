package com.example.encryption.dto;

/**
 * Response DTO returned from encryption/decryption API.
 */
public class CryptoResponse {

    private String result;
    private String status;

    public CryptoResponse() {}

    public CryptoResponse(String result, String status) {
        this.result = result;
        this.status = status;
    }

    // Getters and Setters

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
