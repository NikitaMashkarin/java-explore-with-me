package ru.practicum.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.CustomUserDetails;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/me")
    public UserDto me(@AuthenticationPrincipal CustomUserDetails user) {

        return new UserDto(
                user.getId(),
                user.getUsername()
        );
    }
}
