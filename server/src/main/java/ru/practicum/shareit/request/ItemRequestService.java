package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(ItemRequestDto itemRequest, Long requesterId);

    List<ItemRequestDto> getByRequesterId(Long requesterId);

    List<ItemRequestDto> getOthers(Long userId);

    ItemRequestDto getById(Long requestId);
}
