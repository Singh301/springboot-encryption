package com.example.encryption.exception;

import com.example.encryption.dto.CryptoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Global exception handler for centralized error handling.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EncryptionException.class)
    public ResponseEntity<CryptoResponse> handleEncryptionException(EncryptionException ex) {
        return new ResponseEntity<>(
                new CryptoResponse(null, "Encryption error: " + ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(DecryptionException.class)
    public ResponseEntity<CryptoResponse> handleDecryptionException(DecryptionException ex) {
        return new ResponseEntity<>(
                new CryptoResponse(null, "Decryption error: " + ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CryptoResponse> handleGenericException(Exception ex) {
        return new ResponseEntity<>(
                new CryptoResponse(null, "Unexpected error: " + ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
