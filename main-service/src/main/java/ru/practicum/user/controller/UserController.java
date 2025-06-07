package ru.practicum.user.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.NewUserRequestDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private UserService service;

    @GetMapping("/admin/users")
    public List<UserDto> getUsers(@RequestParam List<Long> ids,
                                  @RequestParam @Positive Integer from,
                                  @RequestParam @Positive Integer size) {
        log.info("GET /admin/users");
        return service.getUsers(ids, from, size);
    }

    @PostMapping("/admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody NewUserRequestDto newUserRequestDto) {
        log.info("POST /admin/users");
        return service.addUser(newUserRequestDto);
    }

    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable @Positive Long userId) {
        log.info("DELETE /admin/users/{}", userId);
        service.deleteUser(userId);
    }
}
