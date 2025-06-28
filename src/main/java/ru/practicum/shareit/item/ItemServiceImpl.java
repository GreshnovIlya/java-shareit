package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
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
    public Item create(Item item, Long owner) {
        if (!userStorage.getUsers().containsKey(owner)) {
            throw new NotFoundException(String.format("Пользователь с id = %s не найден", owner));
        }
        Item newItem = itemStorage.create(item, userStorage.getUsers().get(owner));
        log.info("Создан пользователь: {}", newItem);
        return newItem;
    }

    @Override
    public Item update(Item item, Long owner, Long itemId) {
        if (!userStorage.getUsers().containsKey(owner)) {
            throw new NotFoundException(String.format("Пользователь с id = %s не найден", owner));
        }
        Item updateItem = itemStorage.update(item, userStorage.getUsers().get(owner), itemId);
        log.info("Обновлен пользователь: {}", updateItem);
        return updateItem;
    }

    @Override
    public Item get(Long id) {
        return itemStorage.get(id);
    }

    @Override
    public List<Item> getAllItemByOwner(Long owner) {
        return itemStorage.getAllItemByOwner(userStorage.getUsers().get(owner));
    }

    @Override
    public List<Item> getItemsByNameOrDescription(String text) {
        return itemStorage.getItemsByNameOrDescription(text);
    }
}
