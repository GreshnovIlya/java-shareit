package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public UserDto create(User user) {
        UserDto newUser = userStorage.create(user);
        log.info("Создан пользователь: {}", newUser);
        return newUser;
    }

    @Override
    public UserDto update(Long id, User user) {
        UserDto updateUser = userStorage.update(id, user);
        log.info("Обновлен пользователь: {}", updateUser);
        return updateUser;
    }

    @Override
    public UserDto get(Long id) {
        return userStorage.get(id);
    }

    @Override
    public List<UserDto> getAll() {
        return userStorage.getAll();
    }

    @Override
    public void remove(Long id) {
        userStorage.remove(id);
        log.info("Удален пользователь с id = {}", id);
    }
}