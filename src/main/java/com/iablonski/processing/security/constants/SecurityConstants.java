package com.iablonski.processing.security.constants;

public class SecurityConstants {
    public static final String SIGN_UP_URLS = "/api/auth/**"; // авторизация под ЮРЛ
    public static final String SECRET_JWT = "MySecretKeygenJSONWebTokenSecureAndConfirmed";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
    public static final long EXPIRATION_TIME = 36_000_000; // 60min
}