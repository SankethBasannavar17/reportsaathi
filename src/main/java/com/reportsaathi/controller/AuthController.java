package com.reportsaathi.controller;

import com.reportsaathi.dto.AuthResponse;
import com.reportsaathi.dto.LoginRequest;
import com.reportsaathi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController — handles all authentication endpoints.
 *
 * POST /api/auth/login
 *   Body: { "firebaseToken": "eyJ...", "preferredLanguage": "hi" }
 *   Returns: { "token": "eyJ...", "userId": "...", "name": "...", "isNewUser": true }
 *
 * Both login and register are handled by the same endpoint because:
 *   - First-time login → we create the user (isNewUser: true)
 *   - Subsequent login → we find existing user (isNewUser: false)
 * This simplifies the mobile app — it always calls /login, never /register separately.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/login
     *
     * @Valid triggers validation on LoginRequest fields (@NotBlank etc.)
     * If validation fails, GlobalExceptionHandler returns a 400 with field errors.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/register — alias for /login (same behavior).
     * Kept separate so the API is semantically clear in documentation.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
