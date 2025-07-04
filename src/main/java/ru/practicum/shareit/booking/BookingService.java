package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    Booking create(NewBookingDto newBooking, Long userId);

    Booking confirmation(Long ownerId, Long bookingId, boolean approved);

    Booking get(Long userId, Long bookingId);

    List<Booking> getAllBookingUser(Long userId, State state);

    List<Booking> getAllBookingOwner(Long ownerId, State state);
}
