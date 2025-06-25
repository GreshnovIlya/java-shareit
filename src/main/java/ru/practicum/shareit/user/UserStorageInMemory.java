package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EmailExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserStorageInMemory implements UserStorage {
    private Map<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @Override
    public UserDto create(User user) {
        if (emailExists(user.getEmail())) {
            throw new EmailExistException(String.format("Email %s уже существует", user.getEmail()));
        }
        id++;
        user.setId(id);
        users.put(id, user);
        return UserDtoMapper.toUserDto(user);
    }

    @Override
    public UserDto update(Long id, User user) {
        if (emailExists(user.getEmail())) {
            throw new EmailExistException(String.format("Email %s уже существует", user.getEmail()));
        }
        if (!users.containsKey(id)) {
            throw new NotFoundException(String.format("Пользователь с id = %s не найден", id));
        }
        User updateUser = users.get(id);
        if (user.getName() != null && !user.getName().isBlank()) {
            updateUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            updateUser.setEmail(user.getEmail());
        }
        return UserDtoMapper.toUserDto(updateUser);
    }

    @Override
    public UserDto get(Long id) {
        return UserDtoMapper.toUserDto(users.get(id));
    }

    @Override
    public List<UserDto> getAll() {
        return users.values().stream().map(UserDtoMapper::toUserDto).toList();
    }

    @Override
    public void remove(Long id) {
        users.remove(id);
    }

    @Override
    public boolean emailExists(String email) {
        return !users.values().stream().filter(item -> item.getEmail().equals(email)).toList().isEmpty();
    }

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }
}