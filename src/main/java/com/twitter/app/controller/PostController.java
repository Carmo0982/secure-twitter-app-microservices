package com.twitter.app.controller;
import com.twitter.app.dto.CreatePostRequestDTO;
import com.twitter.app.dto.PostResponseDTO;
import com.twitter.app.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear un post")
    @SecurityRequirement(name = "bearer-jwt")
    public PostResponseDTO create(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreatePostRequestDTO request) {
        return postService.create(jwt.getSubject(), request.content());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un post por ID")
    public PostResponseDTO getById(@PathVariable UUID id) {
        return postService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar un post propio")
    @SecurityRequirement(name = "bearer-jwt")
    public void delete(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id) {
        postService.delete(id, jwt.getSubject());
    }
}