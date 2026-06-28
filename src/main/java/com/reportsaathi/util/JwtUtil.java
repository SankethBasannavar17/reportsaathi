package com.reportsaathi.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * JwtUtil — all JWT operations in one place.
 *
 * A JWT (JSON Web Token) is a self-contained string like:
 *   header.payload.signature
 * The payload holds userId + firebaseUid. The signature proves
 * the token was issued by us (using our secret key).
 *
 * Flow:
 *   1. User logs in with Firebase token → we verify it → issue our JWT
 *   2. App stores our JWT locally
 *   3. Every API call sends: Authorization: Bearer <our JWT>
 *   4. JwtAuthFilter calls extractUserId() to identify the caller
 */
@Component
public class JwtUtil {

    // Read from application.properties: jwt.secret
    @Value("${jwt.secret}")
    private String secret;

    // Read from application.properties: jwt.expiration (default 7 days in ms)
    @Value("${jwt.expiration}")
    private long expirationMs;

    /**
     * Build the HMAC-SHA256 signing key from our secret string.
     * Key must be at least 32 characters long.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generate a JWT for a logged-in user.
     *
     * @param userId      Our internal DB UUID for the user
     * @param firebaseUid Their Firebase UID (stored as extra claim for reference)
     * @return Signed JWT string — send this to the app
     */
    public String generateToken(UUID userId, String firebaseUid) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
            .subject(userId.toString())               // "sub" claim = our user UUID
            .claim("firebaseUid", firebaseUid)        // extra claim
            .issuedAt(now)                            // "iat" = when issued
            .expiration(expiry)                       // "exp" = when it expires
            .signWith(getSigningKey())                // sign with HS256
            .compact();                               // build the string
    }

    /**
     * Parse the JWT and return the user's DB UUID.
     * This is called by JwtAuthFilter on every incoming request.
     *
     * @throws JwtException if token is invalid, expired, or tampered with
     */
    public UUID extractUserId(String token) {
        Claims claims = parseClaims(token);
        return UUID.fromString(claims.getSubject());
    }

    /**
     * Check if a token is valid (not expired, signature correct).
     * Returns false instead of throwing — safer to use in filters.
     */
    public boolean isTokenValid(String token) {
        try {
            Claims claims = parseClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // ── Private helper ────────────────────────────────────────────────────
    private Claims parseClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
