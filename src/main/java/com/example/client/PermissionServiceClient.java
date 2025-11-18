package com.example.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

/**
 * Feign client for Permission Service.
 *
 * This client communicates with the permission-service microservice.
 * When running in Docker, use the service name 'permission-service' as the URL.
 * When running locally, use 'localhost:8083'.
 */
@FeignClient(
    name = "permission-service",
    url = "${feign.client.permission-service.url:http://permission-service:8080}",
    configuration = FeignClientConfig.class
)
public interface PermissionServiceClient {

    /**
     * Get permissions for a specific user from the permission service.
     *
     * @param userId The user ID
     * @return List of permissions for the user
     */
    @GetMapping("/api/permissions/user/{userId}")
    List<String> getPermissionsByUserId(@PathVariable("userId") UUID userId);

    /**
     * Add a permission for a specific user in the permission service.
     *
     * @param userId The user ID
     * @param permission The permission to add
     * @return Response entity
     */
    @PostMapping("/api/permissions/user/{userId}")
    ResponseEntity<Void> addPermission(@PathVariable("userId") UUID userId, @RequestBody String permission);
}

