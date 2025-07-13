package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.validation.UpdateValidationGroup;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@Valid @RequestBody NewItemDto item,
                                             @Positive @RequestHeader("X-Sharer-User-Id") long owner)
                                                                                            throws BadRequestException {
        if (item.getAvailable() == null) {
            throw new BadRequestException("Доступ не может быть null");
        }
        log.info("Запрос на создание вещи: {} пользователем с id = {}", item, owner);
        return itemClient.createItem(item, owner);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@Validated(UpdateValidationGroup.class) @RequestBody ItemDto item,
                                             @Positive @RequestHeader("X-Sharer-User-Id") Long owner,
                                             @Positive @PathVariable Long itemId) {
        log.info("Запрос на обновление вещи  с id = {}", itemId);
        return itemClient.updateItem(item, owner, itemId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@Positive @PathVariable long id) {
        log.info("Запрос на получение вещи с id = {}", id);
        return itemClient.getItemById(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemByOwner(@Positive @RequestHeader("X-Sharer-User-Id") Long owner) {
        log.info("Запрос на получение вещей владельца с id = {}", owner);
        return itemClient.getAllItemByOwner(owner);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsByNameOrDescription(@RequestParam String text) {
        log.info("Запрос на получение всех вещей с текстом: {} в названии или в описании", text);
        return itemClient.getItemsByNameOrDescription(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDto commentDto,
                                                @Positive @RequestHeader("X-Sharer-User-Id") Long authorId,
                                                @Positive @PathVariable Long itemId) {
        log.info("Запрос на создание коммента: {} пользователем с id = {} к вещи {}", commentDto, authorId, itemId);
        return itemClient.createComment(commentDto, authorId, itemId);
    }
}
