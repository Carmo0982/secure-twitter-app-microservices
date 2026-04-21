package com.twitter.app.controller;
import com.twitter.app.dto.PostResponseDTO;
import com.twitter.app.services.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stream")
@RequiredArgsConstructor
public class StreamController {

    private final FeedService feedService;

    @GetMapping
    public Page<PostResponseDTO> getStream(Pageable pageable) {
        return feedService.getGlobalFeed(pageable);
    }
}
