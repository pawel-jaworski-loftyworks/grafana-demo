package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Profile("user-service")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getAllUsers() {
        log.info("Fetching all users");
        Collection<User> users = userService.getAllUsers();
        log.info("Found {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        log.info("Fetching user by id: {}", id);
        return userService.getUserById(id)
                .map(user -> {
                    log.info("User found: {}", user.getUsername());
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    log.warn("User not found with id: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }
}

