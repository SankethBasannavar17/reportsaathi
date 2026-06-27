package com.reportsaathi.exception;

/**
 * ReportProcessingException — thrown when anything in the report pipeline fails:
 *   - Image is unreadable
 *   - OCR returns empty text
 *   - AI API call times out or returns bad JSON
 *   - No lab values found in the report
 *
 * Extends RuntimeException so we don't have to declare it in every method signature.
 */
public class ReportProcessingException extends RuntimeException {

    public ReportProcessingException(String message) {
        super(message);
    }

    public ReportProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
