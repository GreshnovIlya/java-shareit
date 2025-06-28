package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public class UserDtoMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
