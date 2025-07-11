package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.dto.State;

import java.util.List;

public interface BookingService {
    BookingDto create(NewBookingDto newBooking, Long userId);

    BookingDto confirmation(Long ownerId, Long bookingId, boolean approved);

    BookingDto get(Long userId, Long bookingId);

    List<BookingDto> getAllBookingUser(Long userId, State state);

    List<BookingDto> getAllBookingOwner(Long ownerId, State state);
}
