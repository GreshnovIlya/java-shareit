package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.UpdateValidationGroup;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Имя не должно быть пустым", groups = Default.class)
    private String name;

    @NotBlank(message = "Почта не должна быть пустой", groups = Default.class)
    @Email(message = "Почта должна содержать '@'",
            groups = {Default.class, UpdateValidationGroup.class})
    private String email;
}
