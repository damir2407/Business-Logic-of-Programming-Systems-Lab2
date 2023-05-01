package com.example.blps_lab1.service;

import com.example.blps_lab1.config.jwt.JwtUtils;
import com.example.blps_lab1.dto.request.RefreshTokenRequest;
import com.example.blps_lab1.dto.response.NewTokenResponse;
import com.example.blps_lab1.exception.TokenHasExpiredException;
import com.example.blps_lab1.model.Jwt;
import com.example.blps_lab1.repository.basic.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class RefreshTokenService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    public RefreshTokenService( UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    public Jwt createRefreshToken(String login){
        return new Jwt(jwtUtils.getLoginFromJwtToken(login));
    }
    public NewTokenResponse createNewToken(RefreshTokenRequest refreshTokenRequest) {
        if (!jwtUtils.validateJwtToken(refreshTokenRequest.getRefreshToken())){
            throw new TokenHasExpiredException(refreshTokenRequest.getRefreshToken(),"Токен истек");
        }
        String login = jwtUtils.getLoginFromJwtToken(refreshTokenRequest.getRefreshToken());
        Jwt token = new Jwt(jwtUtils.generateJWToken(login));
        Jwt refreshToken = createRefreshToken(login);
        return new NewTokenResponse(token.getToken(), refreshToken.getToken());
    }
}
