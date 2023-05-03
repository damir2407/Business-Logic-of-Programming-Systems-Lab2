package com.example.blps_lab1.service;

import com.example.blps_lab1.security.JwtUtils;
import com.example.blps_lab1.dto.request.RefreshTokenRequest;
import com.example.blps_lab1.dto.response.NewTokenResponse;
import com.example.blps_lab1.exception.TokenHasExpiredException;
import com.example.blps_lab1.security.CookUserDetailsService;
import org.springframework.stereotype.Service;


@Service
public class RefreshTokenService {
    private final JwtUtils jwtUtils;

    private final CookUserDetailsService cookUserDetailsService;

    public RefreshTokenService(CookUserDetailsService cookUserDetailsService, JwtUtils jwtUtils) {
        this.cookUserDetailsService = cookUserDetailsService;
        this.jwtUtils = jwtUtils;
    }

    public NewTokenResponse createNewToken(RefreshTokenRequest refreshTokenRequest) {
        if (!jwtUtils.validateJwtToken(refreshTokenRequest.getRefreshToken())) {
            throw new TokenHasExpiredException(refreshTokenRequest.getRefreshToken(), "Токен истек");
        }
        String login = jwtUtils.getLoginFromJwtToken(refreshTokenRequest.getRefreshToken());

        String accessToken = jwtUtils.generateJWTToken(login,
                jwtUtils.getAuthoritiesFromToken(refreshTokenRequest.getRefreshToken()));
        String refreshToken = jwtUtils.generateRefreshToken(
                login, jwtUtils.getAuthoritiesFromToken(refreshTokenRequest.getRefreshToken()));
        return new NewTokenResponse(accessToken, refreshToken);
    }
}