package com.example.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;

/**
 * Feign request interceptor that forwards the requestId from MDC to outgoing requests.
 * 
 * This interceptor reads the requestId from the MDC (Mapped Diagnostic Context) and
 * adds it as a header to all outgoing Feign client requests. This enables request
 * tracing across microservices.
 */
public class FeignRequestIdInterceptor implements RequestInterceptor {

    private static final String REQUEST_ID_HEADER = "requestId";
    private static final String REQUEST_ID_MDC_KEY = "requestId";

    @Override
    public void apply(RequestTemplate template) {
        // Get requestId from MDC
        String requestId = MDC.get(REQUEST_ID_MDC_KEY);
        
        // If requestId is present in MDC, add it to the request header
        if (requestId != null && !requestId.trim().isEmpty()) {
            template.header(REQUEST_ID_HEADER, requestId);
        }
    }
}

