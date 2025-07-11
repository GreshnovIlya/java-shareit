package ru.practicum.shareit.request;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto create(@RequestBody ItemRequestDto itemRequestDto,
                                 @RequestHeader("X-Sharer-User-Id") Long requesterId) {
        return itemRequestService.create(itemRequestDto, requesterId);
    }

    @GetMapping
    public List<ItemRequestDto> getByRequesterId(@RequestHeader("X-Sharer-User-Id") Long requesterId) {
        return itemRequestService.getByRequesterId(requesterId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getOthers(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getOthers(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@PathVariable Long requestId) {
        return itemRequestService.getById(requestId);
    }
}
