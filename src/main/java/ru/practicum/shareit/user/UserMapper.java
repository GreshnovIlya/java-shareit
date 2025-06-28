package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public class UserMapper {
    public static User toUser(UserDto userDto) {
        return new User(
                0L,
                userDto.getName(),
                userDto.getEmail()
        );
    }
}
