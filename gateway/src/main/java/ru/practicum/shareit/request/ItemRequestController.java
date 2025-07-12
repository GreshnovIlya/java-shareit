package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                             @Positive @RequestHeader("X-Sharer-User-Id") Long requesterId) {
        log.info("Запрос на создание запроса вещи: {} пользователем с id = {}", itemRequestDto, requesterId);
        return itemRequestClient.createRequest(itemRequestDto, requesterId);
    }

    @GetMapping
    public ResponseEntity<Object> getByRequesterId(@Positive @RequestHeader("X-Sharer-User-Id") Long requesterId) {
        log.info("Запрос на получение запросов арендатора с id = {}", requesterId);
        return itemRequestClient.getByRequesterId(requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOthers(@Positive @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Запрос на получение запросов других пользователей, без пользователя с id = {}", userId);
        return itemRequestClient.getOthers(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@Positive @PathVariable Long requestId) {
        log.info("Запрос на получение запроса с id = {}", requestId);
        return itemRequestClient.getById(requestId);
    }
}
