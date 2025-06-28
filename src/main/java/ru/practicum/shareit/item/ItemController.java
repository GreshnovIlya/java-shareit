package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validation.UpdateValidationGroup;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long owner,
                          @Valid @RequestBody ItemDto item) {
        return ItemDtoMapper.toItemDto(itemService.create(ItemMapper.toItem(item), owner));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long owner, @PathVariable Long itemId,
                          @Validated(UpdateValidationGroup.class) @RequestBody ItemDto item) {
        return ItemDtoMapper.toItemDto(itemService.update(ItemMapper.toItem(item), owner, itemId));
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable Long id) {
        return ItemDtoMapper.toItemDto(itemService.get(id));
    }

    @GetMapping
    public List<ItemDto> getAllItemByOwner(@RequestHeader("X-Sharer-User-Id") Long owner) {
        return itemService.getAllItemByOwner(owner).stream().map(ItemDtoMapper::toItemDto).toList();
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByNameOrDescription(@RequestParam String text) {
        return itemService.getItemsByNameOrDescription(text).stream().map(ItemDtoMapper::toItemDto).toList();
    }
}