package com.example.blps_lab1.config.jwt;

import com.example.blps_lab1.security.CookUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Value("${token.JWTExpirationMS}")
    private long jwtExpirationMs;
    @Value("${token.RTExpirationMs}")
    private long refreshExpirationMS;

    public String generateToken(String login, long time) {
        Instant now = Instant.now();
        ZoneId utcZone = ZoneId.of("UTC");
        ZonedDateTime utcNow = ZonedDateTime.ofInstant(now, utcZone);
        ZonedDateTime utcExpiration = utcNow.plus(Duration.ofMillis(time));
        return Jwts.builder()
                .setSubject(login)
                .setIssuer("coocker.ru")
                .setIssuedAt(Date.from(utcNow.toInstant()))
                .setExpiration(Date.from(utcExpiration.toInstant()))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateJWToken(Authentication authentication) {
        CookUserDetails userPrincipal = (CookUserDetails) authentication.getPrincipal();
        return generateJWToken(userPrincipal.getUsername());
    }

    public String generateJWToken(String login) {
        return generateToken(login, jwtExpirationMs);
    }

    public String generateRefreshToken(String login) {
        return generateToken(login, refreshExpirationMS);
    }

    public String generateRefreshToken(Authentication authentication) {
        CookUserDetails userPrincipal = (CookUserDetails) authentication.getPrincipal();
        return generateRefreshToken(userPrincipal.getUsername());
    }

    public boolean validateJwtToken(String jwt) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            return true;
        } catch (MalformedJwtException | IllegalArgumentException e) {
            log.error(e.getMessage());
        }
        return false;
    }


    public String getLoginFromJwtToken(String jwt) {

        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody().getSubject();
    }

}
