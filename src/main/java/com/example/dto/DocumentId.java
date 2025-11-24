package com.example.dto;

import java.util.UUID;

public record DocumentId(UUID uuid) {
    public static DocumentId of(UUID uuid) {
        return new DocumentId(uuid);
    }
}
