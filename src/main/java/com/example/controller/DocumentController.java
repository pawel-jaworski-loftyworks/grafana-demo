package com.example.controller;

import com.example.client.PermissionServiceClient;
import com.example.client.UserServiceClient;
import com.example.exception.InsufficientPrivilegesException;
import com.example.model.User;
import com.example.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final PermissionServiceClient permissionServiceClient;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public DocumentController(DocumentService documentService, UserServiceClient userServiceClient, PermissionServiceClient permissionServiceClient) {
        this.documentService = documentService;
        this.userServiceClient = userServiceClient;
        this.permissionServiceClient = permissionServiceClient;
    }

    @GetMapping("/user/{userId}")
    public List<String> getDocumentsByUserId(@PathVariable UUID userId) {
        User user = userServiceClient.getUserById(userId);
        log.info("Fetching documents for user: {} {}", userId, user.username());
        List<String> permissions = permissionServiceClient.getPermissionsByUserId(userId);
        if (permissions == null || permissions.contains("document-read")) {
            log.info("User has no permission to fetch documents");
            throw new InsufficientPrivilegesException("User has no permission to fetch documents");
        }

        return documentService.getDocumentsByUserId(userId);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Void> addDocument(@PathVariable UUID userId, @RequestBody String document) {
        documentService.addDocument(userId, document);
        return ResponseEntity.ok().build();
    }
}

