package com.reportsaathi.util;

import com.reportsaathi.exception.ReportProcessingException;
import org.springframework.stereotype.Component;

import java.util.Base64;

/**
 * ImageUtil — utility for validating and decoding base64 images.
 * Used in Session 3 (OCR pipeline).
 */
@Component
public class ImageUtil {

    // Max image size we accept: 10MB in bytes
    private static final int MAX_BYTES = 10 * 1024 * 1024;

    /**
     * Decode a base64 string to raw bytes.
     * Strips the "data:image/jpeg;base64," prefix if present (from React Native).
     *
     * @throws ReportProcessingException if the string is invalid or too large
     */
    public byte[] decodeBase64Image(String base64Input) {
        if (base64Input == null || base64Input.isBlank()) {
            throw new ReportProcessingException("Image data is empty.");
        }

        // Strip data URI prefix (React Native often sends: "data:image/jpeg;base64,/9j/...")
        String base64Data = base64Input;
        if (base64Input.contains(",")) {
            base64Data = base64Input.substring(base64Input.indexOf(',') + 1);
        }

        try {
            byte[] bytes = Base64.getDecoder().decode(base64Data);
            if (bytes.length > MAX_BYTES) {
                throw new ReportProcessingException(
                    "Image too large (" + (bytes.length / 1024 / 1024) + "MB). Max allowed: 10MB.");
            }
            return bytes;
        } catch (IllegalArgumentException e) {
            throw new ReportProcessingException("Invalid base64 image data.");
        }
    }

    /**
     * Quick sanity check — does it look like a valid base64 image?
     */
    public boolean isValidBase64Image(String base64Input) {
        try {
            decodeBase64Image(base64Input);
            return true;
        } catch (ReportProcessingException e) {
            return false;
        }
    }
}
