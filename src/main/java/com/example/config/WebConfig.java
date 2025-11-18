package com.example.config;

import com.example.interceptor.RequestIdInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC Configuration to register custom interceptors.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RequestIdInterceptor requestIdInterceptor;

    public WebConfig(RequestIdInterceptor requestIdInterceptor) {
        this.requestIdInterceptor = requestIdInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register the RequestIdInterceptor for all paths
        registry.addInterceptor(requestIdInterceptor)
                .addPathPatterns("/**");
    }
}

