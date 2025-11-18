package com.example.service;

import com.example.client.PermissionServiceClient;
import com.example.client.UserServiceClient;
import com.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Example service that demonstrates how to use Feign clients
 * to aggregate data from multiple microservices.
 * 
 * This service is only active when the 'document-service' profile is active,
 * as an example of how one service can call other services.
 */
@Service
@Profile("document-service")
public class UserPermissionAggregatorService {
    
    private static final Logger log = LoggerFactory.getLogger(UserPermissionAggregatorService.class);
    
    private final UserServiceClient userServiceClient;
    private final PermissionServiceClient permissionServiceClient;
    
    public UserPermissionAggregatorService(
            UserServiceClient userServiceClient,
            PermissionServiceClient permissionServiceClient) {
        this.userServiceClient = userServiceClient;
        this.permissionServiceClient = permissionServiceClient;
    }
    
    /**
     * Get all users with their permissions.
     * This demonstrates calling multiple services and aggregating the results.
     * 
     * @return Map of User to their permissions
     */
    public Map<User, List<String>> getAllUsersWithPermissions() {
        log.info("Fetching all users with their permissions");
        
        Map<User, List<String>> userPermissionsMap = new HashMap<>();
        
        try {
            // Call user-service to get all users
            ResponseEntity<Collection<User>> usersResponse = userServiceClient.getAllUsers();
            
            if (usersResponse.getBody() != null) {
                Collection<User> users = usersResponse.getBody();
                log.info("Retrieved {} users from user-service", users.size());
                
                // For each user, get their permissions from permission-service
                for (User user : users) {
                    try {
                        ResponseEntity<List<String>> permissionsResponse = 
                            permissionServiceClient.getPermissionsByUserId(user.getId());
                        
                        List<String> permissions = permissionsResponse.getBody() != null 
                            ? permissionsResponse.getBody() 
                            : Collections.emptyList();
                        
                        userPermissionsMap.put(user, permissions);
                        log.debug("User {} has {} permissions", user.getUsername(), permissions.size());
                        
                    } catch (Exception e) {
                        log.error("Error fetching permissions for user {}: {}", 
                            user.getId(), e.getMessage());
                        userPermissionsMap.put(user, Collections.emptyList());
                    }
                }
            }
            
        } catch (Exception e) {
            log.error("Error fetching users: {}", e.getMessage(), e);
        }
        
        return userPermissionsMap;
    }
    
    /**
     * Get a specific user with their permissions.
     * 
     * @param userId The user ID
     * @return Optional containing the user and their permissions, or empty if not found
     */
    public Optional<Map.Entry<User, List<String>>> getUserWithPermissions(UUID userId) {
        log.info("Fetching user {} with permissions", userId);
        
        try {
            // Call user-service to get the user
            ResponseEntity<User> userResponse = userServiceClient.getUserById(userId);
            
            if (userResponse.getBody() != null) {
                User user = userResponse.getBody();
                
                // Call permission-service to get the user's permissions
                ResponseEntity<List<String>> permissionsResponse = 
                    permissionServiceClient.getPermissionsByUserId(userId);
                
                List<String> permissions = permissionsResponse.getBody() != null 
                    ? permissionsResponse.getBody() 
                    : Collections.emptyList();
                
                log.info("User {} has {} permissions", user.getUsername(), permissions.size());
                
                return Optional.of(Map.entry(user, permissions));
            }
            
        } catch (Exception e) {
            log.error("Error fetching user {} with permissions: {}", userId, e.getMessage(), e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Add a permission to a user.
     * This demonstrates calling a POST endpoint via Feign client.
     * 
     * @param userId The user ID
     * @param permission The permission to add
     * @return true if successful, false otherwise
     */
    public boolean addPermissionToUser(UUID userId, String permission) {
        log.info("Adding permission {} to user {}", permission, userId);
        
        try {
            // First verify the user exists
            ResponseEntity<User> userResponse = userServiceClient.getUserById(userId);
            
            if (userResponse.getBody() != null) {
                // Add the permission
                permissionServiceClient.addPermission(userId, permission);
                log.info("Successfully added permission {} to user {}", permission, userId);
                return true;
            } else {
                log.warn("User {} not found", userId);
                return false;
            }
            
        } catch (Exception e) {
            log.error("Error adding permission to user {}: {}", userId, e.getMessage(), e);
            return false;
        }
    }
}

