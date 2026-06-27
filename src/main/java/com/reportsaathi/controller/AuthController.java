package com.reportsaathi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * AuthController — handles login and registration.
 * STUB: Fully implemented in Session 2.
 *
 * POST /api/auth/login   — verify Firebase token, return our JWT
 * POST /api/auth/register — create new user in DB
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {
        // TODO Session 2: verify Firebase token, issue JWT
        return ResponseEntity.ok(Map.of("message", "Auth — Session 2"));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> body) {
        // TODO Session 2: create user, issue JWT
        return ResponseEntity.ok(Map.of("message", "Register — Session 2"));
    }
}
