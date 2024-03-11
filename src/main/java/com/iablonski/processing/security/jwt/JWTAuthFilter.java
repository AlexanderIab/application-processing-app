package com.iablonski.processing.security.jwt;

import com.iablonski.processing.exception.MissingTokenException;
import com.iablonski.processing.security.constants.SecurityConstants;
import com.iablonski.processing.security.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class JWTAuthFilter extends OncePerRequestFilter {

    private JWTTokenProvider jwtTokenProvider;
    private UserDetailsServiceImpl userDetailsService;
    private TokenBlacklist tokenBlacklist;

    @Autowired
    public void setTokenBlacklist(TokenBlacklist tokenBlacklist) {
        this.tokenBlacklist = tokenBlacklist;
    }

    @Autowired
    public void setJwtUtils(JWTTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJWT(request);

            if (SecurityConstants.LOGOUT_URLS.equalsIgnoreCase(request.getRequestURI())) {
                handleLogoutRequest(jwt, response);
                return;
            }

            if (SecurityConstants.SIGN_UP_URLS.equalsIgnoreCase(request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return;
            }

            handleAuthentication(jwt, response, request);
        } catch (MissingTokenException ex) {
            handleAuthenticationException(ex, response);
        } catch (Exception exception) {
            log.error("Could not set user authentication", exception);
        }

        filterChain.doFilter(request, response);
    }

    private void handleLogoutRequest(String jwt, HttpServletResponse response) throws IOException {
        if (jwt == null || jwt.isBlank()) {
            throw new MissingTokenException("Token is missing");
        } else if (jwtTokenProvider.validateJWToken(jwt)) {
            tokenBlacklist.addTokenToBlacklist(jwt);
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("You have been successfully logged out");
        }
    }

    private void handleAuthentication(String jwt, HttpServletResponse response, HttpServletRequest request) throws IOException {
        if (jwt == null || jwt.isBlank()) {
            throw new MissingTokenException("Token is missing");
        }

        if (tokenBlacklist.isTokenBlacklisted(jwt)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("The token is expired. Please login again");
        } else if (StringUtils.hasText(jwt) && jwtTokenProvider.validateJWToken(jwt)) {
            UUID userId = jwtTokenProvider.getIdFromJWToken(jwt);
            UserDetails userDetails = userDetailsService.loadUserById(userId);
            UsernamePasswordAuthenticationToken authentication
                    = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private void handleAuthenticationException(MissingTokenException ex, HttpServletResponse response) throws IOException {
        log.error("Authentication credentials not found: {}", ex.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Authentication credentials not found. Please provide a token.");
    }

    private String parseJWT(HttpServletRequest request) {
        String headerAuth = request.getHeader(SecurityConstants.HEADER_STRING);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return headerAuth.substring(7);
        }
        return null;
    }
}