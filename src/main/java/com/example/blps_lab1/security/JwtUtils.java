package com.example.blps_lab1.security;

import com.example.blps_lab1.security.CookUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
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

    public String generateToken(CookUserDetails cookUserDetails, long time) {
        Instant now = Instant.now();
        ZoneId utcZone = ZoneId.of("UTC");
        ZonedDateTime utcNow = ZonedDateTime.ofInstant(now, utcZone);
        ZonedDateTime utcExpiration = utcNow.plus(Duration.ofMillis(time));

        Claims claims = Jwts.claims().setSubject(cookUserDetails.getUsername());
        claims.put("roles", cookUserDetails.getAuthorities());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("coocker.ru")
                .setIssuedAt(Date.from(utcNow.toInstant()))
                .setExpiration(Date.from(utcExpiration.toInstant()))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateJWTToken(Authentication authentication) {
        CookUserDetails userPrincipal = (CookUserDetails) authentication.getPrincipal();
        return generateJWTToken(userPrincipal);
    }

    public String generateJWTToken(CookUserDetails cookUserDetails) {
        return generateToken(cookUserDetails, jwtExpirationMs);
    }

    public String generateRefreshToken(CookUserDetails cookUserDetails) {
        return generateToken(cookUserDetails, refreshExpirationMS);
    }

    public boolean validateJwtToken(String jwt) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }


    public String getLoginFromJwtToken(String jwt) {

        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody().getSubject();
    }

}