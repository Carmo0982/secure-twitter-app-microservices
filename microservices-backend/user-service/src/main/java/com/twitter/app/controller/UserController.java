package com.twitter.app.controller;
import com.twitter.app.dto.UserResponseDTO;
import com.twitter.app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserResponseDTO getUser(@PathVariable UUID id) {
        return userService.findById(id);
    }
}
