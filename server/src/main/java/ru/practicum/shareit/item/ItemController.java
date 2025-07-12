package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;

import java.util.List;
import java.util.Map;

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
    public ItemDto create(@RequestBody NewItemDto item,
                          @RequestHeader("X-Sharer-User-Id") Long owner) {
        return itemService.create(item, owner);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto item,
                          @RequestHeader("X-Sharer-User-Id") Long owner, @PathVariable Long itemId) {
        return itemService.update(item, owner, itemId);
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
    public List<ItemDto> getItemsByNameOrDescription(@RequestParam Map<String, Object> parameters) {
        return itemService.getItemsByNameOrDescription(parameters.get("text").toString());
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@RequestBody CommentDto commentDto,
                          @RequestHeader("X-Sharer-User-Id") Long authorId, @PathVariable Long itemId) {
        return itemService.createComment(commentDto, authorId, itemId);
    }
}