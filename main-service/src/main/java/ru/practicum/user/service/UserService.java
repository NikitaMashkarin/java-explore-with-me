package ru.practicum.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.user.dto.NewUserRequestDto;
import ru.practicum.user.dto.UserDto;

import java.util.List;

@Service
public interface UserService {
    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    UserDto addUser(NewUserRequestDto newUserRequestDto);

    void deleteUser(Long userId);
}
