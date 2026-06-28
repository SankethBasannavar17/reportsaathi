package com.reportsaathi.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.reportsaathi.dto.AuthResponse;
import com.reportsaathi.dto.LoginRequest;
import com.reportsaathi.exception.ReportProcessingException;
import com.reportsaathi.model.User;
import com.reportsaathi.repository.UserRepository;
import com.reportsaathi.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AuthService — the brain of the login flow.
 *
 * Exact steps every time a user logs in:
 *   1. Receive Firebase ID token from app
 *   2. Send it to Firebase Admin SDK → Firebase verifies it's genuine
 *   3. Firebase returns: uid, name, email, photoUrl
 *   4. We check if uid already exists in our DB
 *      → If yes: update name (in case they changed it), return existing user
 *      → If no: create new user row in DB
 *   5. Generate our own JWT using JwtUtil
 *   6. Return AuthResponse with the JWT + user info
 *
 * @Transactional ensures DB save and read happen in one atomic operation.
 * @RequiredArgsConstructor = Lombok generates constructor that @Autowires all final fields.
 */
@Service
@RequiredArgsConstructor
@Slf4j  // Lombok: creates a logger → use log.info(), log.error(), etc.
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    /**
     * Main login method — called by AuthController.
     *
     * @param request Contains the Firebase ID token from the app
     * @return AuthResponse with our JWT + user profile data
     */
    @Transactional
    public AuthResponse login(LoginRequest request) {
        // ── Step 1: Verify Firebase token ────────────────────────────────
        FirebaseToken firebaseToken = verifyFirebaseToken(request.getFirebaseToken());

        String firebaseUid = firebaseToken.getUid();
        String name = firebaseToken.getName();       // from Google account
        String email = firebaseToken.getEmail();     // may be null for phone auth

        log.info("Login attempt for Firebase UID: {}", firebaseUid);

        // ── Step 2: Find or create user in our DB ─────────────────────────
        boolean isNewUser = false;
        User user = userRepository.findByFirebaseUid(firebaseUid).orElse(null);

        if (user == null) {
            // First time login → create new user
            isNewUser = true;
            user = User.builder()
                .firebaseUid(firebaseUid)
                .name(name != null ? name : "User")
                .preferredLanguage(
                    request.getPreferredLanguage() != null
                        ? request.getPreferredLanguage()
                        : "en"
                )
                .build();
            user = userRepository.save(user);
            log.info("New user created: {}", user.getId());
        } else {
            // Returning user → update name if it changed in Google account
            if (name != null && !name.equals(user.getName())) {
                user.setName(name);
                userRepository.save(user);
            }
            log.info("Existing user logged in: {}", user.getId());
        }

        // ── Step 3: Generate our JWT ───────────────────────────────────────
        String jwt = jwtUtil.generateToken(user.getId(), firebaseUid);

        // ── Step 4: Build and return response ─────────────────────────────
        return AuthResponse.builder()
            .token(jwt)
            .tokenType("Bearer")
            .userId(user.getId().toString())
            .name(user.getName())
            .preferredLanguage(user.getPreferredLanguage())
            .isNewUser(isNewUser)
            .build();
    }

    /**
     * Update a user's preferred language (called from UserController).
     */
    @Transactional
    public void updateLanguage(String userId, String language) {
        userRepository.findById(java.util.UUID.fromString(userId))
            .ifPresent(user -> {
                user.setPreferredLanguage(language);
                userRepository.save(user);
                log.info("Language updated to {} for user {}", language, userId);
            });
    }

    // ── Private helpers ───────────────────────────────────────────────────

    /**
     * Calls Firebase Admin SDK to verify the token.
     * Firebase checks: signature, expiry, issuer.
     *
     * Throws ReportProcessingException (HTTP 422) if the token is bad.
     * This prevents anyone from sending a fake/expired Firebase token.
     */
    private FirebaseToken verifyFirebaseToken(String idToken) {
        // If Firebase wasn't initialized (no credentials in dev), give a clear error
        if (FirebaseApp.getApps().isEmpty()) {
            throw new ReportProcessingException(
                "Firebase is not configured. Add firebase-credentials.json to src/main/resources/");
        }

        try {
            // checkRevoked=false for performance — add true in high-security scenarios
            return FirebaseAuth.getInstance().verifyIdToken(idToken, false);
        } catch (Exception e) {
            log.error("Firebase token verification failed: {}", e.getMessage());
            throw new ReportProcessingException(
                "Invalid or expired Firebase token. Please sign in again.");
        }
    }
}
