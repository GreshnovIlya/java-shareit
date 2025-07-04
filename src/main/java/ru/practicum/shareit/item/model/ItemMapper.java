package ru.practicum.shareit.item.model;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public class ItemMapper {
    public static Item toItem(ItemDto itemDto) {
        return new Item(
                0L,
                new User(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getRequest()
        );
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest()
        );
    }

    public static ItemCommentDto toItemCommentDto(Item item, Booking next, Booking last, List<CommentDto> comments) {
        return new ItemCommentDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest(),
                next,
                last,
                comments
        );
    }
}
