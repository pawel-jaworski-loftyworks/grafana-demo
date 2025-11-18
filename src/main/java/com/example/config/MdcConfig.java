package com.example.config;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Configuration to set up MDC (Mapped Diagnostic Context) with appId at application startup.
 * This ensures appId is always present in logs, not just during HTTP requests.
 */
@Component
public class MdcConfig implements ApplicationRunner {

    private static final String APP_ID_MDC_KEY = "appId";

    private final String appId;

    public MdcConfig(@Value("${spring.profiles.active:default}") String activeProfile) {
        this.appId = activeProfile;
    }

    @Override
    public void run(ApplicationArguments args) {
        // Set appId in MDC at application startup
        MDC.put(APP_ID_MDC_KEY, appId);
    }
}

