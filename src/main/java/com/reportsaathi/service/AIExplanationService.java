package com.reportsaathi.service;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * AIExplanationService — sends parsed lab values to GPT-4o, returns plain-language explanations.
 * STUB: Fully implemented in Session 5.
 */
@Service
@Slf4j
public class AIExplanationService {

    private final OkHttpClient httpClient;
    private final String apiKey;
    private final String apiUrl;
    private final String model;

    public AIExplanationService(
            OkHttpClient httpClient,
            @Qualifier("openAiApiKey") String apiKey,
            @Qualifier("openAiApiUrl") String apiUrl,
            @Qualifier("openAiModel") String model) {
        this.httpClient = httpClient;
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.model = model;
    }

    public List<Map<String, Object>> explainValues(
            List<Map<String, Object>> parsedValues, String language) {
        // TODO Session 5: build prompt, call OpenAI, parse JSON response
        throw new UnsupportedOperationException("Implemented in Session 5");
    }
}
