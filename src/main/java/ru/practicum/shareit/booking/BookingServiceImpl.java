package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.State;
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
    public Booking create(NewBookingDto newBooking, Long userId) {
        /*if (!newBooking.getStart().isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Начало брони не может в прошлом");
        }
        if (!newBooking.getEnd().isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Конец брони не может в прошлом");
        }*/
        if (newBooking.getEnd().isBefore(newBooking.getStart())) {
            throw new BadRequestException("Конец брони не может быть раньше начала брони");
        }
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id %s не найден", userId)));
        Item item = itemRepository.findById(newBooking.getItemId()).orElseThrow(
                () -> new NotFoundException(String.format("Вещь с id = %s не найден", newBooking.getItemId())));
        if (!item.getAvailable()) {
            throw new BadRequestException(String.format("Вещь с id = %s нельзя забронировать", item.getId()));
        }
        Booking booking = BookingMapper.toBooking(newBooking, user, item);
        bookingRepository.save(booking);
        log.info("Вещь с id = {} запрошена пользователем с id = {}", item.getId(), userId);
        return booking;
    }

    @Override
    public Booking confirmation(Long ownerId, Long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new NotFoundException(String.format("Бронь с id = %s не найдена", bookingId)));
        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow(
                () -> new NotFoundException(String.format("Вещь с id = %s не найден", booking.getItem().getId())));
        if (!Objects.equals(item.getOwner().getId(), ownerId)) {
            throw new NotOwnerException("Одобрить бронь может только владелец вещи");
        }
        if (approved) {
            booking.setStatus(StatusBooking.APPROVED);
        } else {
            booking.setStatus(StatusBooking.REJECTED);
        }
        log.info("Вещь с id = {} забронирована пользователем с id = {}", item.getId(), booking.getBooker().getId());
        bookingRepository.save(booking);
        return booking;
    }

    @Override
    public Booking get(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new NotFoundException(String.format("Бронь с id = %s не найдена", bookingId)));
        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow(
                () -> new NotFoundException(String.format("Вещь с id = %s не найден", booking.getItem().getId())));
        if (!Objects.equals(item.getOwner().getId(), userId) && !Objects.equals(booking.getBooker().getId(), userId)) {
            throw new NotOwnerException("Просмотреть информацию о брони может только владелец вещи или ее арендатор");
        }
        return booking;
    }

    @Override
    public List<Booking> getAllBookingUser(Long userId, State state) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id %s не найден", userId)));
        switch (state) {
            case ALL -> {
                return bookingRepository.findByBookerOrderByStartDesc(user);
            }
            case CURRENT -> {
                return bookingRepository.findByBookerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(user,
                        LocalDateTime.now(), LocalDateTime.now());
            }
            case PAST -> {
                return bookingRepository.findByBookerAndEndIsBeforeOrderByStartDesc(user, LocalDateTime.now());
            }
            case FUTURE -> {
                return bookingRepository.findByBookerAndStartIsAfterOrderByStartDesc(user, LocalDateTime.now());
            }
            case WAITING -> {
                return bookingRepository.findByBookerAndStatusOrderByStartDesc(user, StatusBooking.WAITING);
            }
            case REJECTED -> {
                return bookingRepository.findByBookerAndStatusOrderByStartDesc(user,
                        StatusBooking.REJECTED);
            }
            default -> {
                return List.of();
            }
        }
    }

    @Override
    public List<Booking> getAllBookingOwner(Long ownerId, State state) {
        User owner = userRepository.findById(ownerId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id %s не найден", ownerId)));
        if (itemRepository.findByOwner(owner).isEmpty()) {
            return List.of();
        }
        if (!Objects.equals(itemRepository.findByOwner(owner).getFirst().getOwner().getId(), ownerId)) {
            throw new NotOwnerException("Просмотреть информацию о бронях может только владелец вещи=");
        }
        switch (state) {
            case ALL -> {
                return bookingRepository.findByItemOwnerOrderByStartDesc(owner);
            }
            case CURRENT -> {
                return bookingRepository.findByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(owner,
                        LocalDateTime.now(), LocalDateTime.now());
            }
            case PAST -> {
                return bookingRepository.findByItemOwnerAndEndIsBeforeOrderByStartDesc(owner, LocalDateTime.now());
            }
            case FUTURE -> {
                return bookingRepository.findByItemOwnerAndStartIsAfterOrderByStartDesc(owner, LocalDateTime.now());
            }
            case WAITING -> {
                return bookingRepository.findByItemOwnerAndStatusOrderByStartDesc(owner, StatusBooking.WAITING);
            }
            case REJECTED -> {
                return bookingRepository.findByItemOwnerAndStatusOrderByStartDesc(owner, StatusBooking.REJECTED);
            }
            default -> {
                return List.of();
            }
        }
    }
}
