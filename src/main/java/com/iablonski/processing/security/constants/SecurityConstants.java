package com.iablonski.processing.security.constants;

public class SecurityConstants {
    public static final String LOGOUT_URLS = "/api/auth/logout";
    public static final String SIGN_UP_URLS = "/api/auth/login";
    public static final String SECRET_JWT = "MySecretKeygenJSONWebTokenSecureAndConfirmed";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
    public static final long EXPIRATION_TIME = 1_200_000; // 20 мин
}