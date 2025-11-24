package com.example.service;

import com.example.client.DocumentServiceClient;
import com.example.client.PermissionServiceClient;
import com.example.client.UserServiceClient;
import com.example.dto.Document;
import com.example.dto.DocumentId;
import com.example.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Full integration test that starts the Spring Boot application with all three profiles
 * (user-service, document-service, permission-service) and tests the complete workflow:
 * 1. Creating a user via UserService HTTP endpoint
 * 2. Verifying permissions are added via PermissionService HTTP endpoint
 * 3. Adding documents via DocumentService HTTP endpoint
 * 4. Retrieving documents via DocumentService HTTP endpoint
 *
 * This test uses real HTTP calls with JSON serialization/deserialization.
 * All three services run in the same JVM and communicate via Feign clients pointing to localhost.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles({"test", "user-service", "document-service", "permission-service"})
class UserAndDocumentServiceIntegrationTest {

    @Autowired
    private DocumentServiceClient documentServiceClient;

    @Autowired
    private UserServiceClient userserviceClient;

    @Autowired
    private PermissionServiceClient permissionServiceClient;

    @Test
    void shouldCreateUserAndAddDocument() throws Exception {

        // Given: A new user to create
        User newUser = User.builder()
                .username("testuser")
                .firstName("Test")
                .lastName("User")
                .email("test.user@example.com")
                .build();


        User createdUser = userserviceClient.createUser(
                newUser
        );

        // Then: User should be created successfully
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.id()).isNotNull();
        assertThat(createdUser.username()).isEqualTo("testuser");
        assertThat(createdUser.firstName()).isEqualTo("Test");
        assertThat(createdUser.lastName()).isEqualTo("User");
        assertThat(createdUser.email()).isEqualTo("test.user@example.com");

        // And: User should be retrievable by ID
        User getResponse = userserviceClient.getUserById(
                createdUser.id()
        );

        // And: Permissions should have been added for the user
        List<String> permissions = permissionServiceClient.getPermissionsByUserId(
                createdUser.id()
        );
        assertThat(permissions).contains("document-read");

        // When: Adding a document for the created user
        DocumentId documentId = DocumentId.of(UUID.randomUUID());
        Document document = new Document(documentId, "This is a test document");

        var addDocResponse = documentServiceClient.addDocument(createdUser.id(), document);

        // Then: Document should be added successfully
        assertThat(addDocResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // And: Document should be retrievable for the user
        List<Document> userDocuments = documentServiceClient.getDocumentsByUserId(createdUser.id());

        assertThat(userDocuments).hasSize(1);
        assertThat(userDocuments.get(0).id().uuid()).isEqualTo(documentId.uuid());
        assertThat(userDocuments.get(0).content()).isEqualTo("This is a test document");
    }

}

