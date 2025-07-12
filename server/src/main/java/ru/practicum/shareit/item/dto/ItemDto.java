package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.request.dto.ItemRequestInItemDto;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    @Setter
    private ItemRequestInItemDto request;
}