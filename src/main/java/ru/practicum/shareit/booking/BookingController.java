package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @Valid @RequestBody NewBookingDto newBooking) {
        return BookingMapper.toBookingDto(bookingService.create(newBooking, userId));
    }

    @PatchMapping("/{bookingId}")
    public BookingDto confirmation(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable Long bookingId,
                                   @RequestParam(defaultValue = "false") boolean approved) {
        return BookingMapper.toBookingDto(bookingService.confirmation(ownerId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingDto get(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        return BookingMapper.toBookingDto(bookingService.get(userId, bookingId));
    }

    @GetMapping
    public List<BookingDto> getAllBookingUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getAllBookingUser(userId, state).stream().map(BookingMapper::toBookingDto).toList();
    }

    @GetMapping("owner")
    public List<BookingDto> getAllBookingOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                              @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getAllBookingOwner(ownerId, state).stream().map(BookingMapper::toBookingDto).toList();
    }
}
