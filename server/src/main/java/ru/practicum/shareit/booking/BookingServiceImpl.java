package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository,
                              ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public BookingDto create(NewBookingDto newBooking, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id %s не найден", userId)));
        Item item = itemRepository.findById(newBooking.getItemId()).orElseThrow(
                () -> new NotFoundException(String.format("Вещь с id = %s не найден", newBooking.getItemId())));
        if (!item.getAvailable()) {
            throw new BadRequestException(String.format("Вещь с id = %s нельзя забронировать", item.getId()));
        }
        Booking booking = BookingMapper.toBooking(newBooking, user, item);
        bookingRepository.save(booking);
        //log.info("Вещь с id = {} запрошена пользователем с id = {}", item.getId(), userId);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto confirmation(Long ownerId, Long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new NotFoundException(String.format("Бронь с id = %s не найдена", bookingId)));
        Item item = itemRepository.findById(booking.getItem().getId()).get();
        if (!Objects.equals(item.getOwner().getId(), ownerId)) {
            throw new NotOwnerException("Одобрить бронь может только владелец вещи");
        }
        if (approved) {
            booking.setStatus(StatusBooking.APPROVED);
        } else {
            booking.setStatus(StatusBooking.REJECTED);
        }
        //log.info("Вещь с id = {} забронирована пользователем с id = {}", item.getId(), booking.getBooker().getId());
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto get(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new NotFoundException(String.format("Бронь с id = %s не найдена", bookingId)));
        Item item = itemRepository.findById(booking.getItem().getId()).get();
        if (!Objects.equals(item.getOwner().getId(), userId) && !Objects.equals(booking.getBooker().getId(), userId)) {
            throw new NotOwnerException("Просмотреть информацию о брони может только владелец вещи или ее арендатор");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookingUser(Long userId, State state) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id %s не найден", userId)));
        switch (state) {
            case ALL -> {
                return bookingRepository.findByBookerOrderByStartDesc(user)
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
            case CURRENT -> {
                return bookingRepository.findByBookerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(user,
                        LocalDateTime.now(), LocalDateTime.now())
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
            case PAST -> {
                return bookingRepository.findByBookerAndEndIsBeforeOrderByStartDesc(user, LocalDateTime.now())
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
            case FUTURE -> {
                return bookingRepository.findByBookerAndStartIsAfterOrderByStartDesc(user, LocalDateTime.now())
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
            case WAITING -> {
                return bookingRepository.findByBookerAndStatusOrderByStartDesc(user, StatusBooking.WAITING)
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
            //REJECTED
            default -> {
                return bookingRepository.findByBookerAndStatusOrderByStartDesc(user,
                        StatusBooking.REJECTED)
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
        }
    }

    @Override
    public List<BookingDto> getAllBookingOwner(Long ownerId, State state) {
        User owner = userRepository.findById(ownerId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id %s не найден", ownerId)));
        switch (state) {
            case ALL -> {
                return bookingRepository.findByItemOwnerOrderByStartDesc(owner)
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
            case CURRENT -> {
                return bookingRepository.findByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(owner,
                        LocalDateTime.now(), LocalDateTime.now())
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
            case PAST -> {
                return bookingRepository.findByItemOwnerAndEndIsBeforeOrderByStartDesc(owner, LocalDateTime.now())
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
            case FUTURE -> {
                return bookingRepository.findByItemOwnerAndStartIsAfterOrderByStartDesc(owner, LocalDateTime.now())
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
            case WAITING -> {
                return bookingRepository.findByItemOwnerAndStatusOrderByStartDesc(owner, StatusBooking.WAITING)
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
            //REJECTED
            default -> {
                return bookingRepository.findByItemOwnerAndStatusOrderByStartDesc(owner, StatusBooking.REJECTED)
                        .stream().map(BookingMapper::toBookingDto).toList();
            }
        }
    }
}
