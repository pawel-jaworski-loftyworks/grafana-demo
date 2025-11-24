package com.example.client;

import com.example.dto.Document;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(
        name = "document-service",
        url = "${feign.client.document-service.url:http://document-service:8080}",
        configuration = FeignClientConfig.class
)
public interface DocumentServiceClient {
    @GetMapping("/api/documents/user/{userId}")
    List<Document> getDocumentsByUserId(@PathVariable("userId") UUID userId);

    @PostMapping("/api/documents/user/{userId}")
    ResponseEntity<Void> addDocument(@PathVariable("userId") UUID userId, @RequestBody Document document);
}
