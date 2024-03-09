package com.iablonski.processing.security.jwt;

import com.iablonski.processing.payload.response.InvalidLoginResponse;
import com.iablonski.processing.security.constants.SecurityConstants;
import io.jsonwebtoken.gson.io.GsonSerializer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JWTAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        InvalidLoginResponse loginResponse = new InvalidLoginResponse();

        // Создаем экземпляр GsonSerializer
        GsonSerializer<InvalidLoginResponse> gsonSerializer = new GsonSerializer<>();

        // Преобразуем объект в строку JSON
        String jsonLoginResponse = new String(gsonSerializer.serialize(loginResponse));

        // Устанавливаем заголовок Content-Type и статус ответа
        response.setContentType(SecurityConstants.CONTENT_TYPE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // Отправляем JSON-ответ
        response.getWriter().println(jsonLoginResponse);
    }
}