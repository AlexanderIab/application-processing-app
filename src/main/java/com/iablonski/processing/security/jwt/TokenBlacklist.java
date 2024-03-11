package com.iablonski.processing.security.jwt;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class TokenBlacklist {

    private final Set<String> blacklistedTokens;

    public TokenBlacklist() {
        this.blacklistedTokens = new HashSet<>();
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    public void addTokenToBlacklist(String token) {
        blacklistedTokens.add(token);
    }
}