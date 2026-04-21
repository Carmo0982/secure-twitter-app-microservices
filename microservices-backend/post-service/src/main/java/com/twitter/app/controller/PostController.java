package com.twitter.app.controller;

import com.twitter.app.dto.CreatePostRequestDTO;
import com.twitter.app.dto.PostResponseDTO;
import com.twitter.app.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponseDTO createPost(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CreatePostRequestDTO createPostRequest) {
        return postService.create(jwt.getSubject(), createPostRequest.content());
    }

    @GetMapping("/posts/{id}")
    public PostResponseDTO getPost(@PathVariable UUID id) {
        return postService.findById(id);
    }

    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        postService.delete(id, jwt.getSubject());
    }

    @GetMapping("/users/{id}/posts")
    public Page<PostResponseDTO> getUserPosts(@PathVariable UUID id, Pageable pageable) {
        return postService.findByUser(id, pageable);
    }
}
