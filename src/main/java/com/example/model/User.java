package com.example.model;

import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record User(
        UUID id,
        String username,
        String firstName,
        String lastName,
        String email) {

}
