package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.CommentMapper;
import ru.practicum.shareit.item.model.ItemMapper;
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
    public ItemDto create(@Valid @RequestBody ItemDto item,
                          @RequestHeader("X-Sharer-User-Id") Long owner) {
        return ItemMapper.toItemDto(itemService.create(ItemMapper.toItem(item), owner));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@Validated(UpdateValidationGroup.class) @RequestBody ItemDto item,
                          @RequestHeader("X-Sharer-User-Id") Long owner, @PathVariable Long itemId) {
        return ItemMapper.toItemDto(itemService.update(ItemMapper.toItem(item), owner, itemId));
    }

    @GetMapping("/{id}")
    public ItemCommentDto get(@PathVariable Long id) {
        return itemService.get(id);
    }

    @GetMapping
    public List<ItemCommentDto> getAllItemByOwner(@RequestHeader("X-Sharer-User-Id") Long owner) {
        return itemService.getAllItemByOwner(owner);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByNameOrDescription(@RequestParam String text) {
        return itemService.getItemsByNameOrDescription(text).stream().map(ItemMapper::toItemDto).toList();
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@Valid @RequestBody CommentDto commentDto,
                          @RequestHeader("X-Sharer-User-Id") Long authorId, @PathVariable Long itemId) {
        return CommentMapper.toCommentDto(itemService.createComment(CommentMapper.toComment(commentDto), authorId,
                itemId));
    }
}