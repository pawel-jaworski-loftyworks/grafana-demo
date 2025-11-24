package com.example.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

public record DocumentId(@JsonValue UUID uuid) {

    @JsonCreator
    public static DocumentId of(UUID uuid) {
        return new DocumentId(uuid);
    }
}
