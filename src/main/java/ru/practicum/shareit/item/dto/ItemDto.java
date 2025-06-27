package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;

    @NotBlank(message = "Name не должен быть пустым", groups = Default.class)
    private String name;

    @NotBlank(message = "Description не должен быть пустым", groups = Default.class)
    private String description;

    @NotNull(message = "Available не должен быть пустым", groups = Default.class)
    private Boolean available;

    private ItemRequest request;
}
