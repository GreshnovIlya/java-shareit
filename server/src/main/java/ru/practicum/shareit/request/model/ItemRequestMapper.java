package ru.practicum.shareit.request.model;

import ru.practicum.shareit.item.dto.ItemAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInItemDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestMapper {
    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User user) {
        return new ItemRequest(
                0L,
                itemRequestDto.getDescription(),
                LocalDateTime.now(),
                user
        );
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<ItemAnswerDto> items) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                UserMapper.toUserDto(itemRequest.getRequester()),
                items
        );
    }

    public static ItemRequestInItemDto toItemRequestInItemDto(ItemRequest itemRequest) {
        return new ItemRequestInItemDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                UserMapper.toUserDto(itemRequest.getRequester())
        );
    }
}
