package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.UpdateValidationGroup;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto user) {
        log.info("Создан пользователь: {}", user);
        return userClient.createUser(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@Positive @PathVariable long id,
                                               @Validated(UpdateValidationGroup.class) @RequestBody UserDto user) {
        log.info("Обновлен пользователь с id = {}", id);
        return userClient.updateUser(id, user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@Positive @PathVariable long id) {
        log.info("Получен пользователь с id = {}", id);
        return userClient.getUserById(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Получены все пользователи");
        return userClient.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserById(@Positive @PathVariable long id) {
        log.info("Удален пользователь с id = {}", id);
        return userClient.deleteUserById(id);
    }
}
