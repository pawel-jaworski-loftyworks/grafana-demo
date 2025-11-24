package com.example.service;

import com.example.dto.Document;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Profile("document-service")
public class DocumentService {
    private final ConcurrentHashMap<UUID, List<Document>> userDocuments = new ConcurrentHashMap<>();

    public DocumentService() {
        // Initialize with some sample data
        // Note: In a real application, you'd want to use actual user IDs
    }

    public List<Document> getDocumentsByUserId(UUID userId) {
        return userDocuments.getOrDefault(userId, Collections.emptyList());
    }

    public void addDocument(UUID userId, Document document) {
        userDocuments.computeIfAbsent(userId, k -> new ArrayList<>()).add(document);
    }
}

