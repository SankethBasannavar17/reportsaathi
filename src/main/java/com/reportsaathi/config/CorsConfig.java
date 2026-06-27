package com.reportsaathi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * CorsConfig — Cross-Origin Resource Sharing
 *
 * React Native apps call our backend from a different "origin".
 * Without this, the browser/OS blocks requests for security.
 * We explicitly allow our app to call any endpoint.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Allow requests from any origin (React Native app, Postman, etc.)
        // In prod you could restrict this to your domain
        config.setAllowedOriginPatterns(List.of("*"));

        // HTTP methods our API supports
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Headers the client is allowed to send
        config.setAllowedHeaders(List.of("*"));

        // Allow the Authorization: Bearer <token> header
        config.setAllowCredentials(true);

        // Apply this CORS config to ALL routes
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
