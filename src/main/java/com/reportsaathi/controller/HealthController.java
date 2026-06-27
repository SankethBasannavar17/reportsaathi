package com.reportsaathi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * HealthController — simple liveness check endpoint.
 *
 * Used by:
 *  - You during development to confirm the server started
 *  - Railway/Render health checks to know when to route traffic
 *  - The React Native app to detect if backend is reachable
 *
 * GET /api/health → 200 OK with status JSON (no auth required)
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "app", "ReportSaathi Backend",
            "version", "0.0.1",
            "timestamp", LocalDateTime.now().toString(),
            "message", "Samjho apni report, apni bhaasha mein 🩺"
        ));
    }
}
