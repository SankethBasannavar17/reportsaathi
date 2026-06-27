package com.reportsaathi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityConfig — defines which endpoints are public vs protected.
 *
 * Session 1 opens everything (PERMIT_ALL) so we can test /api/health freely.
 * In Session 2, we'll add JWT filter and tighten this down.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF — we're a stateless REST API, no browser sessions
            .csrf(AbstractHttpConfigurer::disable)

            // Stateless: each request must carry its own JWT (no server-side session)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                // Public endpoints — no token needed
                .requestMatchers("/api/health").permitAll()
                .requestMatchers("/api/auth/**").permitAll()

                // Everything else requires a valid JWT (added in Session 2)
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
