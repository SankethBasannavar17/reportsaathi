package com.reportsaathi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * ReportUploadRequest — the JSON body sent by the app when uploading a lab report image.
 * Fully used in Session 3. Defined now so the project compiles cleanly.
 */
@Data
public class ReportUploadRequest {

    // Base64-encoded image of the lab report (compressed by the app before sending)
    @NotBlank(message = "Image data is required")
    private String imageBase64;

    // Language to use for AI explanations (e.g. "hi", "ta", "en")
    // Falls back to user's profile language if not provided
    private String language;
}
