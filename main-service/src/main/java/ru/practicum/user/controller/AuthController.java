package ru.practicum.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.NewUserRequestDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.CustomUserDetails;
import ru.practicum.user.service.UserService;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/me")
    public UserDto me(@AuthenticationPrincipal CustomUserDetails user) {

        return new UserDto(
                user.getId(),
                user.getUsername()
        );
    }

    @PostMapping("/register")
    public UserDto register(@RequestBody NewUserRequestDto request) {
        return userService.createUser(request);
    }
}
