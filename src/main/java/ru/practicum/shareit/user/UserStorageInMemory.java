package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EmailExistException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserStorageInMemory implements UserStorage {
    private Map<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @Override
    public User create(User user) {
        if (emailExists(user.getEmail()) || String.valueOf(user.getEmail()).equals("null")) {
            throw new EmailExistException(String.format("Email %s уже существует или неверно указан", user.getEmail()));
        }
        id++;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User update(Long id, User user) {
        if (emailExists(user.getEmail())) {
            throw new EmailExistException(String.format("Email %s уже существует или неверно указан", user.getEmail()));
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
        return updateUser;
    }

    @Override
    public User get(Long id) {
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        return users.values().stream().toList();
    }

    @Override
    public void remove(Long id) {
        users.remove(id);
    }

    @Override
    public boolean emailExists(String email) {
        return !users.values().stream().filter(user -> user.getEmail().equals(email)).toList().isEmpty();
    }

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }
}