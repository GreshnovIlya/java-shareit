package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto create(Long owner, Item item);

    ItemDto update(Long owner, Long itemId, Item item);

    ItemDto get(Long id);

    List<ItemDto> getAllItemByOwner(Long owner);

    List<ItemDto> getItemsByNameOrDescription(String text);
}