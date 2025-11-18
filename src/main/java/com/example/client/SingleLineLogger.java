package com.example.client;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Custom Feign logger that outputs all request/response information in a single log line.
 * This is compatible with structured JSON logging and prevents multi-line log entries.
 */
@Slf4j
public class SingleLineLogger extends Logger {

    @Override
    protected void log(String configKey, String format, Object... args) {
        // Use SLF4J for logging to integrate with structured logging
        log.debug(String.format(methodTag(configKey) + format, args));
    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        if (logLevel.ordinal() >= Level.BASIC.ordinal()) {
            StringBuilder builder = new StringBuilder();
            builder.append("Request: ")
                   .append(request.httpMethod())
                   .append(" ")
                   .append(request.url());

            if (logLevel.ordinal() >= Level.HEADERS.ordinal()) {
                builder.append(" | Headers: ");
                for (Map.Entry<String, Collection<String>> entry : request.headers().entrySet()) {
                    builder.append(entry.getKey())
                           .append("=")
                           .append(String.join(",", entry.getValue()))
                           .append("; ");
                }
            }

            if (logLevel.ordinal() >= Level.FULL.ordinal() && request.body() != null) {
                String bodyText = request.charset() != null
                        ? new String(request.body(), request.charset())
                        : new String(request.body());
                builder.append(" | Body: ").append(bodyText.replace("\n", " ").replace("\r", ""));
            }

            log.info("[{}] {}", configKey, builder.toString());
        }
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response,
                                               long elapsedTime) throws IOException {
        if (logLevel.ordinal() >= Level.BASIC.ordinal()) {
            StringBuilder builder = new StringBuilder();
            builder.append("Response: ")
                   .append(response.status())
                   .append(" (")
                   .append(elapsedTime)
                   .append("ms)");

            if (logLevel.ordinal() >= Level.HEADERS.ordinal()) {
                builder.append(" | Headers: ");
                for (Map.Entry<String, Collection<String>> entry : response.headers().entrySet()) {
                    builder.append(entry.getKey())
                           .append("=")
                           .append(String.join(",", entry.getValue()))
                           .append("; ");
                }
            }

            if (logLevel.ordinal() >= Level.FULL.ordinal() && response.body() != null) {
                byte[] bodyData = Util.toByteArray(response.body().asInputStream());
                if (bodyData.length > 0) {
                    String bodyText = Util.decodeOrDefault(bodyData, Util.UTF_8, "Binary data");
                    builder.append(" | Body: ").append(bodyText.replace("\n", " ").replace("\r", ""));
                }
                // FIXME autocloseable
                response = response.toBuilder().body(bodyData).build();
            }

            log.info("[{}] {}", configKey, builder.toString());
        }

        return response;
    }
}

