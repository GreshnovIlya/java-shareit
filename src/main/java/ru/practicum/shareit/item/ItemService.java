package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(Item item, Long owner);

    Item update(Item item, Long owner, Long itemId);

    ItemCommentDto get(Long id);

    List<ItemCommentDto> getAllItemByOwner(Long ownerId);

    List<Item> getItemsByNameOrDescription(String text);

    Comment createComment(Comment comment, Long authorId, Long itemId);
}