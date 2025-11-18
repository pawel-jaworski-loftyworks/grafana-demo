package com.example.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Profile("document-service")
public class DocumentService {
    private final ConcurrentHashMap<UUID, List<String>> userDocuments = new ConcurrentHashMap<>();

    public DocumentService() {
        // Initialize with some sample data
        // Note: In a real application, you'd want to use actual user IDs
        UUID sampleUserId1 = UUID.randomUUID();
        UUID sampleUserId2 = UUID.randomUUID();

        userDocuments.put(sampleUserId1, Arrays.asList(
                "Document 1: Annual Report 2023",
                "Document 2: Project Proposal",
                "Document 3: Meeting Notes"
        ));

        userDocuments.put(sampleUserId2, Arrays.asList(
                "Document 1: Budget Analysis",
                "Document 2: Marketing Strategy"
        ));
    }

    public List<String> getDocumentsByUserId(UUID userId) {
        return userDocuments.getOrDefault(userId, Collections.emptyList());
    }

    public void addDocument(UUID userId, String document) {
        userDocuments.computeIfAbsent(userId, k -> new ArrayList<>()).add(document);
    }
}

