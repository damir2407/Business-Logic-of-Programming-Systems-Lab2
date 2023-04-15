package com.example.blps_lab1.dto.response;

import lombok.Data;

@Data
public class NewTokenResponse {
    private String token;
    private String refreshToken;

    public NewTokenResponse(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
