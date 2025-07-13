package ru.practicum.shareit.booking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.dto.State;

import java.util.List;
import java.util.Map;

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
    public BookingDto create(@RequestBody NewBookingDto newBooking,
                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.create(newBooking, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto confirmation(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable Long bookingId,
                                   @RequestParam Map<String, Object> parameters) {
        return bookingService.confirmation(ownerId, bookingId, parameters.get("approved").equals("true"));
    }

    @GetMapping("/{bookingId}")
    public BookingDto get(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        return bookingService.get(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam Map<String, Object> parameters) {
        return bookingService.getAllBooking(userId, State.from((String) parameters.get("state"))
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + parameters.get("state"))),
                false);
    }

    @GetMapping("owner")
    public List<BookingDto> getAllBookingOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                              @RequestParam Map<String, Object> parameters) {
        return bookingService.getAllBooking(ownerId, State.from((String) parameters.get("state"))
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + parameters.get("state"))),
                true);
    }
}
