package com.reportsaathi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * LoginRequest — the JSON body the React Native app sends to POST /api/auth/login.
 *
 * Flow:
 *   1. User taps "Sign in with Google" on the app
 *   2. Firebase SDK returns an ID token (a long JWT string)
 *   3. App sends that token to us: { "firebaseToken": "eyJ..." }
 *   4. We verify it with Firebase Admin SDK → get user info → issue our own JWT
 *
 * @NotBlank triggers Spring Validation — returns 400 if field is missing/empty.
 */
@Data  // Lombok: generates getters, setters, toString, equals, hashCode
public class LoginRequest {

    @NotBlank(message = "Firebase token is required")
    private String firebaseToken;

    // Optional: user's preferred language, set during onboarding (hi, ta, te, en...)
    private String preferredLanguage = "en";
}
