package com.example.controller;

import com.example.service.PermissionService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/permissions")
@Profile("permission-service")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<String>> getPermissionsByUserId(@PathVariable UUID userId) {
        List<String> permissions = permissionService.getPermissionsByUserId(userId);
        return ResponseEntity.ok(permissions);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Void> addPermission(@PathVariable UUID userId, @RequestBody String permission) {
        permissionService.addPermission(userId, permission);
        return ResponseEntity.ok().build();
    }
}

