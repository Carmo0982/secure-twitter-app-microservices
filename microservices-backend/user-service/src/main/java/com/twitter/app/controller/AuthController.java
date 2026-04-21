package com.twitter.app.controller;
import com.twitter.app.dto.UserResponseDTO;
import com.twitter.app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // Sincroniza el usuario de Auth0 en nuestra BD y devuelve su perfil.
    // El frontend llama a este endpoint justo después del login.
    @PostMapping("/auth/register")
    public UserResponseDTO register(@AuthenticationPrincipal Jwt jwt) {
        return userService.findOrCreate(jwt);
    }

    // Devuelve el perfil del usuario actualmente autenticado.
    @GetMapping("/api/me")
    public UserResponseDTO me(@AuthenticationPrincipal Jwt jwt) {
        return userService.findOrCreate(jwt);
    }
}
