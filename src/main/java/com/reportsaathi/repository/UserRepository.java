package com.reportsaathi.repository;

import com.reportsaathi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * UserRepository — Spring Data JPA handles all SQL automatically.
 *
 * Just by extending JpaRepository, we get:
 *   save(), findById(), findAll(), delete(), count() — for free.
 *
 * We add custom finders below for our specific queries.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Find a user by their Firebase UID.
     * Used during login: Firebase sends us a UID, we find the matching DB user.
     */
    Optional<User> findByFirebaseUid(String firebaseUid);

    /**
     * Check if a firebase UID already exists (for registration flow).
     */
    boolean existsByFirebaseUid(String firebaseUid);
}
