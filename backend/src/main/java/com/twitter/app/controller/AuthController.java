package com.twitter.app.controller;
import com.twitter.app.dto.UserResponseDTO;
import com.twitter.app.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {

    private final UserService userService;

    // Sincroniza el usuario de Auth0 en nuestra BD y devuelve su perfil.
    // El frontend llama a este endpoint justo después del login.
    @PostMapping("/auth/register")
    @Operation(summary = "Registrar / sincronizar usuario tras login con Auth0")
    @SecurityRequirement(name = "bearer-jwt")
    public UserResponseDTO register(@AuthenticationPrincipal Jwt jwt) {
        return userService.findOrCreate(jwt);
    }

    // Devuelve el perfil del usuario actualmente autenticado.
    @GetMapping("/api/me")
    @Operation(summary = "Obtener perfil del usuario autenticado")
    @SecurityRequirement(name = "bearer-jwt")
    public UserResponseDTO me(@AuthenticationPrincipal Jwt jwt) {
        return userService.findOrCreate(jwt);
    }
}