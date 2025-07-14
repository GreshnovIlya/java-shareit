package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.item.dto.ItemAnswerDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ItemRequestDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private UserDto requester;
    private List<ItemAnswerDto> items;
}