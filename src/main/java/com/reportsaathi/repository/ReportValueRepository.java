package com.reportsaathi.repository;

import com.reportsaathi.model.ReportValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * ReportValueRepository — queries for individual test values.
 */
@Repository
public interface ReportValueRepository extends JpaRepository<ReportValue, UUID> {

    /**
     * Get all values for one report (e.g. to show the ResultScreen).
     */
    List<ReportValue> findByReportId(UUID reportId);

    /**
     * Trend query: get values of a specific test (e.g. "HbA1c") for a user,
     * ordered by report date — used to draw the Trend chart in Session 11.
     *
     * JPQL query (not SQL) — uses class/field names, not table/column names.
     */
    @Query("SELECT rv FROM ReportValue rv " +
           "JOIN rv.report r " +
           "WHERE r.user.id = :userId " +
           "AND LOWER(rv.testName) LIKE LOWER(CONCAT('%', :testName, '%')) " +
           "ORDER BY r.reportDate ASC")
    List<ReportValue> findTrendByUserAndTestName(
        @Param("userId") UUID userId,
        @Param("testName") String testName
    );
}
