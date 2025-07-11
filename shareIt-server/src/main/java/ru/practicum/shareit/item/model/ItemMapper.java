package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.ItemAnswerDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemMapper {
    public static Item toItem(ItemDto itemDto) {
        return new Item(
                0L,
                new User(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                null
        );
    }

    public static Item toItem(NewItemDto itemDto) {
        return new Item(
                0L,
                new User(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                null
        );
    }

    public static ItemAnswerDto toItemAnswerDto(Item item) {
        return new ItemAnswerDto(
                item.getId(),
                item.getName(),
                item.getOwner().getId()
        );
    }

    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                null
        );
        setRequestInItem(item.getRequest(), itemDto);
        return itemDto;
    }

    public static ItemCommentDto toItemCommentDto(Item item, LocalDateTime next, LocalDateTime last,
                                                  List<CommentDto> comments) {
        ItemCommentDto itemCommentDto = new ItemCommentDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                null,
                next,
                last,
                comments
        );
        setRequestInItem(item.getRequest(), itemCommentDto);
        return itemCommentDto;
    }

    private static void setRequestInItem(ItemRequest itemRequest, ItemCommentDto itemCommentDto) {
        if (itemRequest != null) {
            itemCommentDto.setRequest(ItemRequestMapper.toItemRequestInItemDto(itemRequest));
        }
    }

    private static void setRequestInItem(ItemRequest itemRequest, ItemDto itemCommentDto) {
        if (itemRequest != null) {
            itemCommentDto.setRequest(ItemRequestMapper.toItemRequestInItemDto(itemRequest));
        }
    }
}
