package com.reportsaathi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ReportExplanationResponse — what the backend returns after processing a report.
 * Fully built in Sessions 3–6. Defined now so the project compiles.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportExplanationResponse {

    private String reportId;
    private String labName;
    private String reportDate;
    private List<ExplainedValue> values;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExplainedValue {
        private String testName;
        private String value;
        private String unit;
        private String normalRange;
        private String status;        // NORMAL, HIGH, LOW, CRITICAL
        private String explanation;   // In user's language
        private String doctorAdvice;
    }
}
