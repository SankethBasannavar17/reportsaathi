package com.reportsaathi.controller;

import com.reportsaathi.model.User;
import com.reportsaathi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * UserController — user profile endpoints. All require a valid JWT.
 *
 * GET  /api/users/me          → return logged-in user's profile
 * PUT  /api/users/language    → update preferred language
 *
 * @AuthenticationPrincipal User user — Spring automatically injects the User object
 * that JwtAuthFilter set in SecurityContext. We never need to parse the JWT here.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    /**
     * GET /api/users/me
     * Returns the logged-in user's profile.
     * Requires: Authorization: Bearer <token>
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getProfile(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(Map.of(
            "id", user.getId().toString(),
            "name", user.getName() != null ? user.getName() : "",
            "phone", user.getPhone() != null ? user.getPhone() : "",
            "preferredLanguage", user.getPreferredLanguage(),
            "createdAt", user.getCreatedAt().toString()
        ));
    }

    /**
     * PUT /api/users/language
     * Body: { "language": "hi" }
     * Updates the user's preferred language for AI explanations.
     */
    @PutMapping("/language")
    public ResponseEntity<Map<String, String>> updateLanguage(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, String> body) {

        String language = body.get("language");
        if (language == null || language.isBlank()) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "language field is required"));
        }

        authService.updateLanguage(user.getId().toString(), language);

        return ResponseEntity.ok(Map.of(
            "message", "Language updated successfully",
            "language", language
        ));
    }
}
