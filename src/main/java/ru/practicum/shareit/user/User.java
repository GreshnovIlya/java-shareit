package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.UpdateValidationGroup;

/**
 * TODO Sprint add-controllers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;

    @NotBlank(message = "Name не должен быть пустым", groups = Default.class)
    private String name;

    @NotBlank(message = "Email не должен быть пустым", groups = Default.class)
    @Email(message = "Email должен содержать '@'",
            groups = {Default.class, UpdateValidationGroup.class})
    private String email;
}