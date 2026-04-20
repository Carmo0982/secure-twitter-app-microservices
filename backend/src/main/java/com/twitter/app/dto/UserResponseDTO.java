package com.twitter.app.dto;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username,
        String email,
        Instant createdAt
) {
}
