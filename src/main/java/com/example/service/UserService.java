package com.example.service;

import com.example.client.PermissionServiceClient;
import com.example.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Profile("user-service")
public class UserService {
    private final ConcurrentHashMap<UUID, User> users = new ConcurrentHashMap<>();
    private final PermissionServiceClient permissionServiceClient;

    public UserService(PermissionServiceClient permissionServiceClient) {
        // Initialize with some sample data
        UUID user1Id = UUID.randomUUID();
        UUID user2Id = UUID.randomUUID();
        UUID user3Id = UUID.randomUUID();

        users.put(user1Id, new User(user1Id, "jdoe", "John", "Doe", "john.doe@example.com"));
        users.put(user2Id, new User(user2Id, "asmith", "Alice", "Smith", "alice.smith@example.com"));
        users.put(user3Id, new User(user3Id, "bwilliams", "Bob", "Williams", "bob.williams@example.com"));
        this.permissionServiceClient = permissionServiceClient;
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public Optional<User> getUserById(UUID id) {
        return Optional.ofNullable(users.get(id));
    }

    public User createUser(User user) {
        var userWithId = user.toBuilder().id(UUID.randomUUID()).build();
        System.out.println("Creating user: " + userWithId.username().toUpperCase());
        users.put(userWithId.id(), userWithId);
        permissionServiceClient.addPermission(userWithId.id(), "document-read");
        return userWithId;
    }
}

