package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemStorageInMemory implements ItemStorage {
    private Map<Long, Item> items = new HashMap<>();
    private Long id = 0L;

    @Override
    public ItemDto create(Long owner, Item item) {
        if (String.valueOf(item.getAvailable()).equals("null")) {
            throw new ArgumentException("Available не может быть null");
        }
        id++;
        item.setId(id);
        item.setOwner(owner);
        items.put(id, item);
        return ItemDtoMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(Long owner, Long itemId, Item item) {
        if (items.get(itemId).getOwner() != owner) {
            throw new NotFoundException(String.format("Пользователь с id = %s не владеет данной вещью", owner));
        }
        Item updateItem = items.get(itemId);
        if (item.getName() != null && !item.getName().isBlank()) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            updateItem.setDescription(item.getDescription());
        }
        if (!String.valueOf(item.getAvailable()).equals("null") && !String.valueOf(item.getAvailable()).isBlank()) {
            updateItem.setAvailable(item.getAvailable());
        }
        updateItem.setAvailable(item.getAvailable());
        return ItemDtoMapper.toItemDto(updateItem);
    }

    @Override
    public ItemDto get(Long id) {
        return ItemDtoMapper.toItemDto(items.get(id));
    }

    @Override
    public List<ItemDto> getAllItemByOwner(Long owner) {
        return items.values().stream().filter(item -> item.getOwner() == owner).map(ItemDtoMapper::toItemDto).toList();
    }

    @Override
    public List<ItemDto> getItemsByNameOrDescription(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return items.values().stream().filter(item -> (
                item.getName().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) ||
                item.getDescription().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)))
                && item.getAvailable()).map(ItemDtoMapper::toItemDto).toList();
    }
}
