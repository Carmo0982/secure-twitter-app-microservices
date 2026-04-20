package com.twitter.app.services;
import com.twitter.app.dto.UserResponseDTO;
import com.twitter.app.entities.User;
import com.twitter.app.exceptions.ResourceNotFoundException;
import com.twitter.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Busca el usuario local por su auth0Sub.
    // Si no existe, lo crea con los datos del JWT.
    public UserResponseDTO findOrCreate(Jwt jwt) {
        String sub = jwt.getSubject();
        String emailClaim = jwt.getClaimAsString("email");
        if (emailClaim == null || emailClaim.isBlank()) {
            emailClaim = sub + "@auth0.local";
        }

        String usernameClaim = firstNonBlank(
                jwt.getClaimAsString("preferred_username"),
                jwt.getClaimAsString("nickname"),
                jwt.getClaimAsString("name")
        );

        if (usernameClaim == null || usernameClaim.isBlank()) {
            usernameClaim = emailClaim.contains("@") ? emailClaim.split("@")[0] : "user";
        }

        final String email = emailClaim;
        final String username = usernameClaim;

        User user = userRepository.findByAuth0Sub(sub).orElseGet(() -> {
            // Si el username ya existe, le añadimos un sufijo único
            String finalUsername = userRepository.existsByUsername(username)
                    ? username + "_" + sub.substring(sub.length() - 4)
                    : username;

            return userRepository.save(
                    User.builder()
                            .auth0Sub(sub)
                            .email(email)
                            .username(finalUsername)
                            .build()
            );
        });

        return toResponse(user);
    }

    // Busca un usuario por su ID (para GET /api/users/{id})
    public UserResponseDTO findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return toResponse(user);
    }

    // Convierte User -> UserResponse
    public UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt());
    }

    // Busca la entidad User por su auth0Sub (usado internamente por PostService)
    public User getEntityByAuth0Sub(String sub) {
        return userRepository.findByAuth0Sub(sub)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}