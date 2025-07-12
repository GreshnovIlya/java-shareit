package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestInItemDto;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;

    @NotBlank(message = "Название не должно быть пустым", groups = Default.class)
    private String name;

    @NotBlank(message = "Описание не должно быть пустым", groups = Default.class)
    private String description;

    @NotNull(message = "Доступ не должен быть пустым", groups = Default.class)
    private Boolean available;

    @Positive
    private ItemRequestInItemDto request;
}
