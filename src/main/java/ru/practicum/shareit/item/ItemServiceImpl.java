package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public ItemDto create(Long owner, Item item) {
        if (!userStorage.getUsers().containsKey(owner)) {
            throw new NotFoundException(String.format("Пользователь с id = %s не найден", owner));
        }
        ItemDto newItem = itemStorage.create(owner, item);
        log.info("Создан пользователь: {}", newItem);
        return newItem;
    }

    @Override
    public ItemDto update(Long owner, Long itemId, Item item) {
        ItemDto updateItem = itemStorage.update(owner, itemId, item);
        log.info("Обновлен пользователь: {}", updateItem);
        return updateItem;
    }

    @Override
    public ItemDto get(Long id) {
        return itemStorage.get(id);
    }

    @Override
    public List<ItemDto> getAllItemByOwner(Long owner) {
        return itemStorage.getAllItemByOwner(owner);
    }

    @Override
    public List<ItemDto> getItemsByNameOrDescription(String text) {
        return itemStorage.getItemsByNameOrDescription(text);
    }
}
