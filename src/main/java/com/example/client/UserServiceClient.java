package com.example.client;

import com.example.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;
import java.util.UUID;

/**
 * Feign client for User Service.
 *
 * This client communicates with the user-service microservice.
 * When running in Docker, use the service name 'user-service' as the URL.
 * When running locally, use 'localhost:8081'.
 *
 * Note: This client returns objects directly instead of ResponseEntity.
 * If a user is not found (404), a FeignException will be thrown.
 */
@FeignClient(
    name = "user-service",
    url = "${feign.client.user-service.url:http://user-service:8080}",
    configuration = FeignClientConfig.class
)
public interface UserServiceClient {

    /**
     * Get all users from the user service.
     *
     * @return Collection of all users
     */
    @GetMapping("/api/users")
    Collection<User> getAllUsers();

    /**
     * Get a specific user by ID from the user service.
     *
     * @param id The user ID
     * @return The user with the specified ID
     * @throws feign.FeignException.NotFound if user is not found (404)
     */
    @GetMapping("/api/users/{id}")
    User getUserById(@PathVariable("id") UUID id);
}

