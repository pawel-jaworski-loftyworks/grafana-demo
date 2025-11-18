package com.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@Profile("permission-service")
public class PermissionService {
    private final ConcurrentHashMap<UUID, List<String>> userPermissions = new ConcurrentHashMap<>();

    public PermissionService() {
        // Initialize with some sample data
        UUID sampleUserId1 = UUID.randomUUID();
        UUID sampleUserId2 = UUID.randomUUID();

        userPermissions.put(sampleUserId1, Arrays.asList(
                "READ",
                "WRITE",
                "DELETE",
                "ADMIN"
        ));

        userPermissions.put(sampleUserId2, Arrays.asList(
                "READ",
                "WRITE"
        ));
    }

    public List<String> getPermissionsByUserId(UUID userId) {
        List<String> permissions = userPermissions.getOrDefault(userId, Collections.emptyList());
        log.info("Permissions for user {} {}", userId, permissions);
        return permissions;
    }

    public void addPermission(UUID userId, String permission) {
        log.info("Adding permission {} for user {}", permission, userId);
        userPermissions.computeIfAbsent(userId, k -> new ArrayList<>()).add(permission);
        log.info("Permission added for user {}. Permissions: {}", userId, userPermissions.get(userId));
    }
}

