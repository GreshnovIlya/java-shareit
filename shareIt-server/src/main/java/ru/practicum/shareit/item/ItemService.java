package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(NewItemDto item, Long owner);

    ItemDto update(ItemDto item, Long owner, Long itemId);

    ItemCommentDto get(Long id);

    List<ItemCommentDto> getAllItemByOwner(Long ownerId);

    List<ItemDto> getItemsByNameOrDescription(String text);

    CommentDto createComment(CommentDto comment, Long authorId, Long itemId);
}