package com.example.controller;

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

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<String>> getDocumentsByUserId(@PathVariable UUID userId) {
        List<String> documents = documentService.getDocumentsByUserId(userId);
        return ResponseEntity.ok(documents);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Void> addDocument(@PathVariable UUID userId, @RequestBody String document) {
        documentService.addDocument(userId, document);
        return ResponseEntity.ok().build();
    }
}

