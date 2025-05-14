package com.example.encryption.controller;

import com.example.encryption.dto.CryptoRequest;
import com.example.encryption.service.CryptoService;
import com.example.encryption.exception.EncryptionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for CryptoController using MockMvc.
 */

@WebMvcTest(CryptoController.class)
public class CryptoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CryptoService cryptoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testEncrypt() throws Exception {
        CryptoRequest request = new CryptoRequest("MyTestKey123", "Hello");
        String mockEncryptedData = "EncryptedMockStringBase64==";
        Mockito.when(cryptoService.encrypt(anyString(), anyString())).thenReturn(mockEncryptedData);

        mockMvc.perform(post("/api/crypto/encrypt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(mockEncryptedData))
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void testDecrypt() throws Exception {
        CryptoRequest request = new CryptoRequest("MyTestKey123", "EncryptedMockStringBase64==");
        String mockDecryptedData = "Hello";
        Mockito.when(cryptoService.decrypt(anyString(), anyString())).thenReturn(mockDecryptedData);

        mockMvc.perform(post("/api/crypto/decrypt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(mockDecryptedData))
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void testEncryptServiceThrowsException() throws Exception {
        CryptoRequest request = new CryptoRequest("TestKey", "Hello");

        doThrow(new EncryptionException("Simulated failure"))
                .when(cryptoService).encrypt(anyString(), anyString());

        mockMvc.perform(post("/api/crypto/encrypt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("Encryption error: Simulated failure"));
    }

    @Test
    void testDecryptServiceThrowsGenericException() throws Exception {
        CryptoRequest request = new CryptoRequest("TestKey", "EncryptedData");

        doThrow(new RuntimeException("Unexpected exception"))
                .when(cryptoService).decrypt(anyString(), anyString());

        mockMvc.perform(post("/api/crypto/decrypt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("Unexpected error: Unexpected exception"));
    }
}
