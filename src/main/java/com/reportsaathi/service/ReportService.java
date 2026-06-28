package com.reportsaathi.service;

import com.reportsaathi.dto.ReportExplanationResponse;
import com.reportsaathi.dto.ReportUploadRequest;
import com.reportsaathi.repository.ReportRepository;
import com.reportsaathi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * ReportService — orchestrates the full upload → OCR → AI → save pipeline.
 * STUB: Fully implemented in Sessions 3–7.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public ReportExplanationResponse processReport(ReportUploadRequest request, UUID userId) {
        // TODO Session 3–6: OCR + AI pipeline
        throw new UnsupportedOperationException("Implemented in Session 3");
    }

    public List<ReportExplanationResponse> getReportHistory(UUID userId) {
        // TODO Session 7
        throw new UnsupportedOperationException("Implemented in Session 7");
    }
}
