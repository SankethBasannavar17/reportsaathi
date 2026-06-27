package com.reportsaathi.repository;

import com.reportsaathi.model.Report;
import com.reportsaathi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * ReportRepository — queries against the "reports" table.
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {

    /**
     * Get all reports for a user, newest first.
     * Used in History screen.
     */
    List<Report> findByUserOrderByCreatedAtDesc(User user);

    /**
     * Count how many reports a user has uploaded total.
     */
    long countByUser(User user);
}
