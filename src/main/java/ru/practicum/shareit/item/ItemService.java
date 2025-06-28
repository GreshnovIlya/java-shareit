package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(Item item, Long owner);

    Item update(Item item, Long owner, Long itemId);

    Item get(Long id);

    List<Item> getAllItemByOwner(Long owner);

    List<Item> getItemsByNameOrDescription(String text);
}