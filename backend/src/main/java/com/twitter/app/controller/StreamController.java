package com.twitter.app.controller;
import com.twitter.app.dto.PostResponseDTO;
import com.twitter.app.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stream")
@RequiredArgsConstructor
@Tag(name = "Stream")
public class StreamController {

    private final PostService postService;

    @GetMapping
    @Operation(summary = "Feed global de posts — público, paginado")
    public Page<PostResponseDTO> getStream(Pageable pageable) {
        return postService.findAll(pageable);
    }
}