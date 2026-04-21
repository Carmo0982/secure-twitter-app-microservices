package com.twitter.app.services;
import com.twitter.app.dto.PostResponseDTO;
import com.twitter.app.dto.UserResponseDTO;
import com.twitter.app.entities.Post;
import com.twitter.app.entities.User;
import com.twitter.app.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final PostRepository postRepository;

    // Feed global paginado
    public Page<PostResponseDTO> getGlobalFeed(Pageable pageable) {
        return postRepository.findAllWithUser(pageable).map(this::toResponse);
    }

    // Convierte Post -> PostResponse
    private PostResponseDTO toResponse(Post post) {
        return new PostResponseDTO(
                post.getId(),
                post.getContent(),
                post.getCreatedAt(),
                toUserResponse(post.getUser())
        );
    }

    private UserResponseDTO toUserResponse(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt());
    }
}
