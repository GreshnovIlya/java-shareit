package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ItemRequestInItemDto {
    private long id;
    private String description;
    private LocalDateTime created;
    private UserDto requester;
}