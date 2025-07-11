package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewBookingDto {
    @NotNull(message = "id вещи не может быть пустой")
    @Positive
    private Long itemId;

    @NotNull(message = "Начало брони не может быть пустой")
    //@Future
    private LocalDateTime start;

    @NotNull(message = "Конец брони не может быть пустой")
    //@Future
    private LocalDateTime end;
}
