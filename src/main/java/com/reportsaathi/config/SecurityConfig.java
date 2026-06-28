package com.reportsaathi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig — Session 2 update: wires JwtAuthFilter into the security chain.
 *
 * Filter execution order for every request:
 *   1. CorsFilter        (allow React Native app to call us)
 *   2. JwtAuthFilter     (validate token, set SecurityContext)
 *   3. Spring Security   (check if SecurityContext has a valid user)
 *   4. Controller method (actually runs)
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Spring injects JwtAuthFilter because it's a @Component
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF — stateless REST API doesn't need it
            .csrf(AbstractHttpConfigurer::disable)

            // Stateless sessions — no server-side session storage
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                // ── Public endpoints (no token required) ──
                .requestMatchers("/api/health").permitAll()
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/auth/register").permitAll()

                // ── Everything else requires a valid JWT ──
                .anyRequest().authenticated()
            )

            // Add our JWT filter BEFORE Spring's default username/password filter.
            // This means: by the time Spring checks auth, we've already set the user in context.
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
