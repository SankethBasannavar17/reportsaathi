package com.reportsaathi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * UserController — handles user profile read/update.
 * STUB: Implemented in Session 2.
 *
 * GET  /api/users/me        — get logged-in user profile
 * PUT  /api/users/language  — update preferred language
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> getProfile() {
        // TODO Session 2
        return ResponseEntity.ok(Map.of("message", "Profile — Session 2"));
    }

    @PutMapping("/language")
    public ResponseEntity<Map<String, String>> updateLanguage(@RequestBody Map<String, String> body) {
        // TODO Session 2
        return ResponseEntity.ok(Map.of("message", "Language updated — Session 2"));
    }
}
