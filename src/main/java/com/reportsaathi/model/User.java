package com.reportsaathi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * User — maps to the "users" table in PostgreSQL.
 *
 * We use Firebase UID as the unique identifier because
 * Firebase handles Google Sign-In. Our backend trusts Firebase tokens
 * and looks up/creates a user by their firebase_uid.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    // Firebase UID — comes from Firebase token, must be unique per user
    @Column(name = "firebase_uid", unique = true, nullable = false, length = 128)
    private String firebaseUid;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "phone", length = 15)
    private String phone;

    // Default language: "en". Possible: hi, ta, te, bn, mr, kn, ml, gu
    @Column(name = "preferred_language", length = 10)
    @Builder.Default
    private String preferredLanguage = "en";

    @Column(name = "created_at", updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
