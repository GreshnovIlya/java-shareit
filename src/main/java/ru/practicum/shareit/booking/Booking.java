package ru.practicum.shareit.booking;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.UpdateValidationGroup;

import java.time.Instant;

/**
 * TODO Sprint add-bookings.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @NotNull(message = "Id cannot be empty", groups = UpdateValidationGroup.class)
    private long id;

    @NotNull(message = "Start cannot be empty", groups = {Default.class, UpdateValidationGroup.class})
    private Instant start;

    @NotNull(message = "Start cannot be empty", groups = {Default.class, UpdateValidationGroup.class})
    private Instant end;

    @NotNull(message = "Item cannot be empty", groups = {Default.class, UpdateValidationGroup.class})
    private long item;

    @NotNull(message = "Booker cannot be empty", groups = {Default.class, UpdateValidationGroup.class})
    private long booker;

    private StatusBooking status;
}
