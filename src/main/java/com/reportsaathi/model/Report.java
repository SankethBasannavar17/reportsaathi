package com.reportsaathi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Report — maps to the "reports" table.
 *
 * One report per lab visit. Contains the raw OCR text (for debugging)
 * and links to all individual ReportValues extracted from it.
 *
 * NOTE: Raw image is NEVER stored here — only text. This is both a
 * storage cost decision and a privacy decision.
 */
@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    // Which user uploaded this report
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Lab name extracted from OCR (e.g. "Dr Lal PathLabs", "SRL Diagnostics")
    @Column(name = "lab_name", length = 200)
    private String labName;

    // Report date extracted from the document
    @Column(name = "report_date")
    private LocalDate reportDate;

    // Full raw text from Google Vision — useful for debugging parsing issues
    @Column(name = "raw_ocr_text", columnDefinition = "TEXT")
    private String rawOcrText;

    @Column(name = "created_at", updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    // One report has many test values (Hemoglobin, WBC, HbA1c, etc.)
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ReportValue> reportValues = new ArrayList<>();
}
