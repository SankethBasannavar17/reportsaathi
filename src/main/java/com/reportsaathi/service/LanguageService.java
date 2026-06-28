package com.reportsaathi.service;

import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * LanguageService — validates and resolves language codes.
 * Used in Session 5 (AI prompt language selection).
 */
@Service
public class LanguageService {

    // Supported language codes → mapped to language names for the AI prompt
    private static final Set<String> SUPPORTED_LANGUAGES = Set.of(
        "en", "hi", "ta", "te", "bn", "mr", "kn", "ml", "gu"
    );

    /**
     * Validate and return a language code.
     * Falls back to English if the code is unknown.
     */
    public String resolveLanguage(String code) {
        if (code != null && SUPPORTED_LANGUAGES.contains(code.toLowerCase())) {
            return code.toLowerCase();
        }
        return "en";
    }

    /**
     * Map a language code to its full name (used in the AI prompt).
     * e.g. "hi" → "Hindi", "ta" → "Tamil"
     */
    public String getLanguageName(String code) {
        return switch (code) {
            case "hi" -> "Hindi";
            case "ta" -> "Tamil";
            case "te" -> "Telugu";
            case "bn" -> "Bengali";
            case "mr" -> "Marathi";
            case "kn" -> "Kannada";
            case "ml" -> "Malayalam";
            case "gu" -> "Gujarati";
            default  -> "English";
        };
    }
}
