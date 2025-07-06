package ru.practicum.shareit.user.model;

import ru.practicum.shareit.user.dto.UserDto;

public class UserMapper {
    public static User toUser(UserDto userDto) {
        return new User(
                0L,
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
