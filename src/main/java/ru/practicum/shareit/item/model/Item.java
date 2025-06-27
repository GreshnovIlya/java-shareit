package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Long id;

    private User owner;

    @NotBlank(message = "Name не должен быть пустым", groups = Default.class)
    private String name;

    @NotBlank(message = "Description не должен быть пустым", groups = Default.class)
    private String description;

    @NotNull(message = "Available не должен быть пустым", groups = Default.class)
    private Boolean available;

    private ItemRequest request;
}