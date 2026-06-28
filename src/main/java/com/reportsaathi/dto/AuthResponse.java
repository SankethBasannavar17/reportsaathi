package com.reportsaathi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AuthResponse — what we return to the app after successful login.
 *
 * The app stores `token` in AsyncStorage and sends it with every request
 * as: Authorization: Bearer <token>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    // Our JWT — the app uses this for all subsequent API calls
    private String token;

    // Token type — always "Bearer" (standard HTTP auth scheme)
    private String tokenType = "Bearer";

    // User's DB UUID — useful for the app to identify the user locally
    private String userId;

    // Display name from Firebase (from Google account)
    private String name;

    // User's preferred language code (e.g. "hi", "ta", "en")
    private String preferredLanguage;

    // true = existing user logged back in, false = new user just registered
    private boolean isNewUser;
}
