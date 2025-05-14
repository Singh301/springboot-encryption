package com.example.encryption.controller;

import com.example.encryption.dto.CryptoRequest;
import com.example.encryption.dto.CryptoResponse;
import com.example.encryption.service.CryptoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller exposing endpoints for encryption and decryption operations.
 */
@RestController
@RequestMapping("/api/crypto")
public class CryptoController {

    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    /**
     * Encrypt endpoint - accepts key and data, returns encrypted result.
     */
    @PostMapping("/encrypt")
    public ResponseEntity<CryptoResponse> encrypt(@Valid @RequestBody CryptoRequest request) {
        String result = cryptoService.encrypt(request.getKey(), request.getData());
        return ResponseEntity.ok(new CryptoResponse(result, "success"));
    }

    /**
     * Decrypt endpoint - accepts key and encrypted data, returns decrypted result.
     */
    @PostMapping("/decrypt")
    public ResponseEntity<CryptoResponse> decrypt(@Valid @RequestBody CryptoRequest request) {
        String result = cryptoService.decrypt(request.getKey(), request.getData());
        return ResponseEntity.ok(new CryptoResponse(result, "success"));
    }

}
