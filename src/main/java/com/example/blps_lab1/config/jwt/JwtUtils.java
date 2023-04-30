package com.example.blps_lab1.config.jwt;

import com.example.blps_lab1.security.CookUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Value("${token.JWTExpirationMS}")
    private int jwtExpirationMs;


    public String generateJwtToken(Authentication authentication) {
        CookUserDetails userPrincipal = (CookUserDetails) authentication.getPrincipal();
        return generateJwtToken(userPrincipal.getUsername());
    }
    //add kid, issuer, role
    public String generateJwtToken(String login){
        return Jwts.builder().setSubject(login).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512).compact();
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
