package com.iablonski.processing.security.jwt;

import com.iablonski.processing.security.constants.SecurityConstants;
import com.iablonski.processing.security.service.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class JWTTokenProvider {

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);

        String userId = userPrincipal.getId().toString();

        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", userId);
        claimsMap.put("username", userPrincipal.getUsername());
        claimsMap.put("authorities", userPrincipal.getAuthorities());

        return Jwts.builder()
                .subject(userId)
                .claims(claimsMap)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyByte = Decoders.BASE64.decode(SecurityConstants.SECRET_JWT);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public boolean validateJWToken(String token) {
        try {
            Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException exception) {
            log.error(exception.getMessage());
            return false;
        }
    }

    public UUID getIdFromJWToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getKey()).build()
                .parseSignedClaims(token)
                .getPayload();
        String id = (String) claims.get("id");
        return UUID.fromString(id);
    }
}