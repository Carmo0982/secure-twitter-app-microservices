package com.twitter.app.services;
import com.twitter.app.dto.PostResponseDTO;
import com.twitter.app.entities.Post;
import com.twitter.app.entities.User;
import com.twitter.app.exceptions.ForbiddenException;
import com.twitter.app.exceptions.ResourceNotFoundException;
import com.twitter.app.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    // Crea un post para el usuario autenticado
    public PostResponseDTO create(String auth0Sub, String content) {
        User user = getUserByAuth0Sub(auth0Sub);

        Post post = postRepository.save(
                Post.builder()
                        .content(content)
                        .user(user)
                        .build()
        );

        return toResponse(post);
    }

    // Obtiene un post por ID
    public PostResponseDTO findById(UUID id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado"));
        return toResponse(post);
    }

    // Elimina un post — solo el autor puede hacerlo
    public void delete(UUID postId, String auth0Sub) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado"));

        if (!post.getUser().getAuth0Sub().equals(auth0Sub)) {
            throw new ForbiddenException("No puedes eliminar un post que no es tuyo");
        }

        postRepository.delete(post);
    }

    // Feed global paginado
    public Page<PostResponseDTO> findAll(Pageable pageable) {
        return postRepository.findAllWithUser(pageable).map(this::toResponse);
    }

    // Posts de un usuario específico
    public Page<PostResponseDTO> findByUser(UUID userId, Pageable pageable) {
        return postRepository.findByUserId(userId, pageable).map(this::toResponse);
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

    private User getUserByAuth0Sub(String auth0Sub) {
        // NOTE: This will be replaced with a call to the user-service
        // For now, we'll mock it
        return new User();
    }

    private com.twitter.app.dto.UserResponseDTO toUserResponse(User user) {
        return new com.twitter.app.dto.UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt());
    }
}
