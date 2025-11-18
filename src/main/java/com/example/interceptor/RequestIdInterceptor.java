package com.example.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * HTTP Interceptor that extracts the requestId from the HTTP header
 * and adds it to the MDC (Mapped Diagnostic Context) for logging.
 */
@Component
public class RequestIdInterceptor implements HandlerInterceptor {

    private static final String REQUEST_ID_HEADER = "requestId";
    private static final String REQUEST_ID_MDC_KEY = "requestId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Get requestId from header, or generate a new one if not present
        String requestId = request.getHeader(REQUEST_ID_HEADER);
        
        if (requestId == null || requestId.trim().isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }
        
        // Add requestId to MDC
        MDC.put(REQUEST_ID_MDC_KEY, requestId);
        
        // Optionally add requestId to response header for tracing
        response.setHeader(REQUEST_ID_HEADER, requestId);
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Clean up MDC after request is complete to prevent memory leaks
        MDC.remove(REQUEST_ID_MDC_KEY);
    }
}

