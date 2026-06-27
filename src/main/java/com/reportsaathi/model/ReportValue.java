package com.reportsaathi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ReportValue — maps to the "report_values" table.
 *
 * Each row is ONE test in a report.
 * Example: Hemoglobin | 11.2 | g/dL | 12.0–17.0 | LOW | "Your blood..."
 */
@Entity
@Table(name = "report_values")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportValue {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    // Parent report
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    // Test name exactly as it appeared on the report (e.g. "Serum Creatinine")
    @Column(name = "test_name", length = 200)
    private String testName;

    // The numeric value (e.g. 1.4)
    @Column(name = "value", precision = 10, scale = 3)
    private BigDecimal value;

    // Unit (e.g. "mg/dL", "g/dL", "%", "cells/µL")
    @Column(name = "unit", length = 50)
    private String unit;

    // Normal range bounds (parsed from report)
    @Column(name = "normal_range_min", precision = 10, scale = 3)
    private BigDecimal normalRangeMin;

    @Column(name = "normal_range_max", precision = 10, scale = 3)
    private BigDecimal normalRangeMax;

    // NORMAL, HIGH, LOW, CRITICAL — set by our parsing + AI logic
    @Column(name = "status", length = 20)
    private String status;

    // English explanation from AI
    @Column(name = "explanation_en", columnDefinition = "TEXT")
    private String explanationEn;

    // Local language explanation from AI (e.g. Hindi, Tamil, etc.)
    @Column(name = "explanation_local", columnDefinition = "TEXT")
    private String explanationLocal;

    @Column(name = "created_at", updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
