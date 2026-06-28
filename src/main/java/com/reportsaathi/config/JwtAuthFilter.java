package com.reportsaathi.config;

import com.reportsaathi.model.User;
import com.reportsaathi.repository.UserRepository;
import com.reportsaathi.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * JwtAuthFilter — runs ONCE per HTTP request, BEFORE Spring Security's auth checks.
 *
 * What it does:
 *   1. Reads "Authorization: Bearer <token>" header
 *   2. Validates the token with JwtUtil
 *   3. Loads the User from DB using the userId in the token
 *   4. Sets the user in SecurityContext so Spring knows who is calling
 *
 * If the token is missing or invalid → security context stays empty
 * → SecurityConfig's .anyRequest().authenticated() rejects the request with 403.
 *
 * Extends OncePerRequestFilter → Spring guarantees it runs exactly once per request.
 * (@Component registers it as a Spring bean so SecurityConfig can add it to the chain.)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // ── Step 1: Extract token from Authorization header ───────────────
        String token = extractToken(request);

        if (token != null && jwtUtil.isTokenValid(token)) {
            // ── Step 2: Get userId from token ─────────────────────────────
            UUID userId;
            try {
                userId = jwtUtil.extractUserId(token);
            } catch (Exception e) {
                log.warn("Failed to extract user ID from token: {}", e.getMessage());
                filterChain.doFilter(request, response);
                return;
            }

            // ── Step 3: Load user from DB ──────────────────────────────────
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                log.warn("Token contains valid userId but user not found in DB: {}", userId);
                filterChain.doFilter(request, response);
                return;
            }

            User user = userOpt.get();

            // ── Step 4: Set Spring Security context ────────────────────────
            // This is how Spring knows "user X is authenticated for this request"
            // We use the User object as the principal (accessible via @AuthenticationPrincipal)
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    user,                       // principal — can be retrieved in controllers
                    null,                       // credentials — null for JWT (no password)
                    Collections.emptyList()     // authorities/roles — empty for now (add RBAC later)
                );

            authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Authenticated user: {} for request: {}", userId, request.getRequestURI());
        }

        // Pass the request down the filter chain regardless
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT from the Authorization header.
     *
     * Header format: "Authorization: Bearer eyJhbGci..."
     * We strip "Bearer " prefix and return just the token string.
     *
     * Returns null if the header is missing or malformed.
     */
    private String extractToken(HttpServletRequest request) {
        String headerValue = request.getHeader("Authorization");

        if (StringUtils.hasText(headerValue) && headerValue.startsWith("Bearer ")) {
            return headerValue.substring(7); // Remove "Bearer " (7 characters)
        }
        return null;
    }
}
