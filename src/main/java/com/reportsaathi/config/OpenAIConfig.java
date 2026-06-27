package com.reportsaathi.config;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * OpenAIConfig — configures the HTTP client used to call OpenAI GPT-4o.
 *
 * We use OkHttp (not Spring's RestTemplate) because it handles
 * large streaming responses and timeouts better for AI API calls.
 */
@Configuration
public class OpenAIConfig {

    // Reads openai.api.key from application.properties
    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${openai.api.url}")
    private String openAiApiUrl;

    @Value("${openai.model}")
    private String openAiModel;

    /**
     * OkHttpClient bean — shared across the app.
     * Timeouts are generous because GPT-4o can take 10-30 seconds
     * when explaining multiple lab values in a regional language.
     */
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)   // wait up to 60s for AI response
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    }

    // Expose as beans so services can @Autowired them
    @Bean(name = "openAiApiKey")
    public String openAiApiKey() {
        return openAiApiKey;
    }

    @Bean(name = "openAiApiUrl")
    public String openAiApiUrl() {
        return openAiApiUrl;
    }

    @Bean(name = "openAiModel")
    public String openAiModel() {
        return openAiModel;
    }
}
