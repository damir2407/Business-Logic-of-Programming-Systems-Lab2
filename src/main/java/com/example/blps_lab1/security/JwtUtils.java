package com.example.blps_lab1.security;

import com.example.blps_lab1.model.basic.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;

@Component
@Slf4j
public class JwtUtils {
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Value("${token.JWTExpirationMS}")
    private long jwtExpirationMs;

    @Value("${token.issuer}")
    private String issuer;
    @Value("${token.RTExpirationMs}")
    private long refreshExpirationMS;

    public String generateToken(String login, Set<Role> roleSet, long time) {
        Instant now = Instant.now();
        ZoneId utcZone = ZoneId.of("UTC");
        ZonedDateTime utcNow = ZonedDateTime.ofInstant(now, utcZone);
        ZonedDateTime utcExpiration = utcNow.plus(Duration.ofMillis(time));

        Claims claims = Jwts.claims().setSubject(login);
        claims.put("authorities", roleSet);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setIssuedAt(Date.from(utcNow.toInstant()))
                .setExpiration(Date.from(utcExpiration.toInstant()))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateJWTToken(String login, Set<Role> roles) {
        return generateToken(login, roles, jwtExpirationMs);
    }


    public String generateRefreshToken(String login, Set<Role> roles) {
        return generateToken(login, roles, refreshExpirationMS);
    }


    public boolean validateJwtToken(String jwt) {
        try {
            if (!Jwts.parserBuilder().setSigningKey(key).build().
                    parseClaimsJws(jwt).getBody().getIssuer().equals(issuer))
                throw new SignatureException("Invalid JWT signature!");
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

    public Set<Role> getAuthoritiesFromToken(String jwt){
        return (Set<Role>) Jwts.parserBuilder().setSigningKey(key).build().
                parseClaimsJws(jwt).getBody().get("authorities");
    }

}