package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NewBookingDto {
    private long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}