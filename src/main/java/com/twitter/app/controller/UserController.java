package com.twitter.app.controller;
import com.twitter.app.dto.PostResponseDTO;
import com.twitter.app.dto.UserResponseDTO;
import com.twitter.app.services.PostService;
import com.twitter.app.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users")
public class UserController {

    private final UserService userService;
    private final PostService postService;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener perfil público de un usuario")
    public UserResponseDTO getUser(@PathVariable UUID id) {
        return userService.findById(id);
    }

    @GetMapping("/{id}/posts")
    @Operation(summary = "Obtener posts de un usuario")
    public Page<PostResponseDTO> getUserPosts(@PathVariable UUID id, Pageable pageable) {
        return postService.findByUser(id, pageable);
    }
}