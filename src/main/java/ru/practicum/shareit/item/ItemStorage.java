package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemStorage {
    Item create(Item item, User owner);

    Item update(Item item, User owner, Long itemId);

    Item get(Long id);

    List<Item> getAllItemByOwner(User owner);

    List<Item> getItemsByNameOrDescription(String text);
}
