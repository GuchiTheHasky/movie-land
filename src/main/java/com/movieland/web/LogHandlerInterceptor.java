package com.movieland.web;

import com.movieland.service.impl.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

import static com.movieland.service.impl.AuthenticationService.BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
public class LogHandlerInterceptor implements HandlerInterceptor {

    private static final String USER_TYPE = "userType";
    private static final String UUID_KEY = "UUID";
    private static final String GUEST = "GUEST";

    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        MDC.put(UUID_KEY, generateRandomUUID());

        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader == null) {
            MDC.put(USER_TYPE, GUEST);
            log.info("Authorization header not found. User type set to {}", GUEST);
        } else {
            String token = extractToken(authHeader);
            String userTypeValue = jwtService.extractUsername(token);
            MDC.put(USER_TYPE, userTypeValue);
            log.info("Authorization header found. User type extracted: {}", userTypeValue);
        }
        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) {
        throw new NotImplementedException("Method \"postHandle(HttpServletRequest, HttpServletResponse, Object, ModelAndView);\" not implemented yet.");
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        throw new NotImplementedException("Method \"afterCompletion(HttpServletRequest, HttpServletResponse, Object, Exception);\" not implemented yet.");
    }

    private String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }

    private String extractToken(String authHeader) {
        return authHeader.replaceFirst(BEARER, StringUtils.EMPTY);
    }
}
