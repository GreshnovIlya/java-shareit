package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    UserDto create(User user);

    UserDto update(Long id, User user);

    UserDto get(Long id);

    List<UserDto> getAll();

    void remove(Long id);

    boolean emailExists(String email);

    Map<Long, User> getUsers();
}
