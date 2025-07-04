package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.UpdateValidationGroup;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserDto user) {
        User a = UserMapper.toUser(user);
        UserDto as = UserDtoMapper.toUserDto(a);
        return UserDtoMapper.toUserDto(userService.create(UserMapper.toUser(user)));
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable Long id, @Validated(UpdateValidationGroup.class) @RequestBody UserDto user) {
        return UserDtoMapper.toUserDto(userService.update(id, UserMapper.toUser(user)));
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        return UserDtoMapper.toUserDto(userService.get(id));
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll().stream().map(UserDtoMapper::toUserDto).toList();
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        userService.remove(id);
    }
}