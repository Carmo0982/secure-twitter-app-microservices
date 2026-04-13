package com.twitter.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePostRequestDTO(
        @NotBlank(message = "El contenido no puede estar vacío")
        @Size(max = 140, message = "Máximo 140 caracteres")
        String content

) {}





