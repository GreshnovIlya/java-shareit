package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(User user);

    UserDto update(Long id, User user);

    UserDto get(Long id);

    List<UserDto> getAll();

    void remove(Long id);
}
