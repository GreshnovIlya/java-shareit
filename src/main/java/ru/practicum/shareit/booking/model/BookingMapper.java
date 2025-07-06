package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

public class BookingMapper {
    public static Booking toBooking(NewBookingDto newBookingDto, User user, Item item) {
        return new Booking(
                0L,
                newBookingDto.getStart(),
                newBookingDto.getEnd(),
                item,
                user,
                StatusBooking.WAITING
        );
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                ItemMapper.toItemDto(booking.getItem()),
                UserMapper.toUserDto(booking.getBooker()),
                booking.getStatus()
        );
    }
}
