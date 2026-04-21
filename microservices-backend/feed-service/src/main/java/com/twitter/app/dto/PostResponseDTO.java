package com.twitter.app.dto;

import java.time.Instant;
import java.util.UUID;

public record PostResponseDTO(
        UUID id,
        String content,
        Instant createdAt,
        UserResponseDTO user
) {
}
