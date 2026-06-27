package com.reportsaathi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * ReportController — handles report upload, OCR, AI explain, and history.
 * STUB: Fully implemented in Sessions 3–7.
 *
 * POST /api/reports/upload  — receive image, run OCR + AI, return explained values
 * GET  /api/reports          — list all reports for logged-in user
 * GET  /api/reports/{id}     — get single report with all values
 * GET  /api/reports/trend    — get trend data for one test name
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> upload() {
        // TODO Session 3+: OCR + AI pipeline
        return ResponseEntity.ok(Map.of("message", "Upload — Session 3"));
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> listReports() {
        // TODO Session 7
        return ResponseEntity.ok(Map.of("message", "History — Session 7"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getReport(@PathVariable String id) {
        // TODO Session 7
        return ResponseEntity.ok(Map.of("message", "Report detail — Session 7"));
    }
}
