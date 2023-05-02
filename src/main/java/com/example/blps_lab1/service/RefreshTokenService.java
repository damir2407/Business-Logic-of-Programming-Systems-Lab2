package com.example.blps_lab1.service;

import com.example.blps_lab1.security.JwtUtils;
import com.example.blps_lab1.dto.request.RefreshTokenRequest;
import com.example.blps_lab1.dto.response.NewTokenResponse;
import com.example.blps_lab1.exception.TokenHasExpiredException;
import com.example.blps_lab1.model.Jwt;
import com.example.blps_lab1.security.CookUserDetails;
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

    public Jwt createRefreshToken(String login) {
        CookUserDetails cookUserDetails = (CookUserDetails) cookUserDetailsService.loadUserByUsername(login);
        return new Jwt(jwtUtils.generateRefreshToken(cookUserDetails.getUsername()));
    }

    public Jwt createRefreshToken(CookUserDetails cookUserDetails) {
        return new Jwt(jwtUtils.generateRefreshToken(cookUserDetails.getUsername()));
    }

    public NewTokenResponse createNewToken(RefreshTokenRequest refreshTokenRequest) {
        if (!jwtUtils.validateJwtToken(refreshTokenRequest.getRefreshToken())) {
            throw new TokenHasExpiredException(refreshTokenRequest.getRefreshToken(), "Токен истек");
        }
        String login = jwtUtils.getLoginFromJwtToken(refreshTokenRequest.getRefreshToken());
        CookUserDetails cookUserDetails = (CookUserDetails) cookUserDetailsService.loadUserByUsername(login);

        Jwt token = new Jwt(jwtUtils.generateJWTToken(cookUserDetails.getUsername()));
        Jwt refreshToken = createRefreshToken(cookUserDetails);
        return new NewTokenResponse(token.getToken(), refreshToken.getToken());
    }
}