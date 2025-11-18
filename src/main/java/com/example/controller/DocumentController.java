package com.example.controller;

import com.example.client.UserServiceClient;
import com.example.model.User;
import com.example.service.DocumentService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
@Profile("document-service")
public class DocumentController {
    private final DocumentService documentService;
    private final UserServiceClient userServiceClient;

    public DocumentController(DocumentService documentService, UserServiceClient userServiceClient) {
        this.documentService = documentService;
        this.userServiceClient = userServiceClient;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<String>> getDocumentsByUserId(@PathVariable UUID userId) {
        ResponseEntity<User> user = userServiceClient.getUserById(userId);
        List<String> documents = documentService.getDocumentsByUserId(userId);
        return ResponseEntity.ok(documents);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Void> addDocument(@PathVariable UUID userId, @RequestBody String document) {
        documentService.addDocument(userId, document);
        return ResponseEntity.ok().build();
    }
}

