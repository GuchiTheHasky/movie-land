package com.movieland.configuration;

import com.movieland.service.impl.DefaultUserDetailsService;
import com.movieland.web.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final DefaultUserDetailsService defaultUserDetailsService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/v1/movies/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/genre/**")
                        .permitAll()
                        .requestMatchers("/api/v1/login/**", "/api/v1/register/**")
                        .permitAll()
                        .requestMatchers("/api/v1/review").hasAuthority("USER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/movies/**")
                        .hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/movies/**")
                        .hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/movies/**")
                        .hasAuthority("ADMIN")
                        .anyRequest()
                        .authenticated()

                ).userDetailsService(defaultUserDetailsService)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
