package ru.practicum.shareit.user;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    User create(User user);

    User update(Long id, User user);

    User get(Long id);

    List<User> getAll();

    void remove(Long id);

    boolean emailExists(String email);

    Map<Long, User> getUsers();
}
