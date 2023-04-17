package com.example.blps_lab1.service;

import com.example.blps_lab1.config.jwt.JwtUtils;
import com.example.blps_lab1.dto.request.LogOutRequest;
import com.example.blps_lab1.dto.request.RefreshTokenRequest;
import com.example.blps_lab1.dto.response.NewTokenResponse;
import com.example.blps_lab1.exception.TokenHasExpiredException;
import com.example.blps_lab1.model.basic.RefreshToken;
import com.example.blps_lab1.repository.basic.RefreshTokenRepository;
import com.example.blps_lab1.repository.basic.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${token.RTExpirationMs}")
    private Long refreshTokenExpirationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, JwtUtils jwtUtils) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(String login) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpirationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(userRepository.findByLogin(login).get());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public NewTokenResponse createNewToken(RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = findByToken(refreshTokenRequest.getRefreshToken()).get();
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new TokenHasExpiredException(refreshToken.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        refreshTokenRepository.deleteByUser(refreshToken.getUser());
        refreshToken = createRefreshToken(refreshToken.getUser().getLogin());
        String token = jwtUtils.generateJwtToken(refreshToken.getUser().getLogin());
        return new NewTokenResponse(token, refreshToken.getToken());
    }

    public int deleteByUserLogin(LogOutRequest logOutRequest) {
        return refreshTokenRepository.deleteByUser(userRepository.findByLogin(logOutRequest.getLogin()).get());
    }
}
