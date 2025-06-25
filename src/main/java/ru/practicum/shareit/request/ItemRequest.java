package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.UpdateValidationGroup;

import java.time.Instant;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    @NotNull(message = "Id cannot be empty", groups = UpdateValidationGroup.class)
    private long id;

    @NotBlank(message = "Description cannot be empty", groups = {Default.class, UpdateValidationGroup.class})
    private String description;

    @NotNull(message = "Requestor cannot be empty", groups = {Default.class, UpdateValidationGroup.class})
    private long requestor;

    private Instant created;
}
