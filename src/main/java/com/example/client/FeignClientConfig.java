package com.example.client;

import feign.Logger;
import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configuration class for Feign clients.
 * 
 * This configuration sets up common settings for all Feign clients including:
 * - Connection and read timeouts
 * - Retry logic
 * - Logging level
 */
@Configuration
public class FeignClientConfig {

    /**
     * Configure request options including timeouts.
     * 
     * @return Request.Options with custom timeout settings
     */
    @Bean
    public Request.Options requestOptions() {
        // Connect timeout: 5 seconds
        // Read timeout: 10 seconds
        return new Request.Options(
            5, TimeUnit.SECONDS,
            10, TimeUnit.SECONDS,
            true
        );
    }

    /**
     * Configure retry logic for failed requests.
     * 
     * @return Retryer with custom retry settings
     */
    @Bean
    public Retryer retryer() {
        // Retry up to 3 times with 1 second interval between retries
        return new Retryer.Default(1000, 1000, 3);
    }

    /**
     * Configure logging level for Feign clients.
     * 
     * @return Logger.Level for Feign client logging
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        // FULL: Log headers, body, and metadata for both request and response
        // Other options: NONE, BASIC, HEADERS
        return Logger.Level.FULL;
    }
}

