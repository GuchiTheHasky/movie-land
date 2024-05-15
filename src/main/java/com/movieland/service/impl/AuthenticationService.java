package com.movieland.service.impl;

import com.movieland.dto.UserRegistrationDto;
import com.movieland.entity.Role;
import com.movieland.entity.User;
import com.movieland.repository.UserRepository;
import com.movieland.web.controller.request.AuthRequest;
import com.movieland.web.controller.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    public static final String BEARER = "Bearer ";

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, String> redisTemplate;

    public AuthenticationResponse register(UserRegistrationDto userRegistrationDto) {
        User user = new User();
        user.setEmail(userRegistrationDto.getEmail());
        user.setNickname(userRegistrationDto.getNickname());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        user.setRole(Role.USER);
        user = repository.save(user);

        String uuid = jwtService.generateToken(user);

        return new AuthenticationResponse(uuid, user.getNickname());
    }

    public AuthenticationResponse authenticate(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        User user = repository.findByEmail(authRequest.getEmail()).orElseThrow();
        String uuid = jwtService.generateToken(user);

        return new AuthenticationResponse(uuid, user.getNickname());
    }

    public void logout(String authHeader) {
        String token = authHeader.replaceFirst(BEARER, StringUtils.EMPTY);
        Date expirationDate = jwtService.extractExpiration(token);
        long timeOut = ((expirationDate.getTime() / 60000) - (Date.from(Instant.now()).getTime() / 60000));
        redisTemplate.opsForValue().set(token, "token", timeOut, TimeUnit.MINUTES);
    }

}
