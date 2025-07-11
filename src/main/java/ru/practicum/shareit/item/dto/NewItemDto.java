package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewItemDto {
    private Long id;

    @NotBlank(message = "Название не должно быть пустым", groups = Default.class)
    private String name;

    @NotBlank(message = "Описание не должно быть пустым", groups = Default.class)
    private String description;

    @NotNull(message = "Доступ не должен быть пустым", groups = Default.class)
    private Boolean available;

    private Long requestId;
}
