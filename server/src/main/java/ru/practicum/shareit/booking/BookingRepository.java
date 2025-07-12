package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerOrderByStartDesc(User booker);

    List<Booking> findByBookerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(User booker, LocalDateTime now,
                                                                            LocalDateTime now1);

    List<Booking> findByBookerAndEndIsBeforeOrderByStartDesc(User booker, LocalDateTime now);

    List<Booking> findByBookerAndStartIsAfterOrderByStartDesc(User booker, LocalDateTime now);


    List<Booking> findByBookerAndStatusOrderByStartDesc(User booker, StatusBooking statusBooking);

    List<Booking> findByItemOwnerOrderByStartDesc(User owner);

    List<Booking> findByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(User owner, LocalDateTime now,
                                                                            LocalDateTime now1);

    List<Booking> findByItemOwnerAndEndIsBeforeOrderByStartDesc(User owner, LocalDateTime now);

    List<Booking> findByItemOwnerAndStartIsAfterOrderByStartDesc(User owner, LocalDateTime now);

    List<Booking> findByItemOwnerAndStatusOrderByStartDesc(User owner, StatusBooking statusBooking);

    List<Booking> findByItemAndStatusAndStartIsAfterOrderByStartDesc(Item item, StatusBooking statusBooking,
                                                                     LocalDateTime now);

    List<Booking> findByItemAndStatusAndBookerAndEndIsBeforeOrderByStartDesc(Item item, StatusBooking statusBooking,
                                                                             User booker, LocalDateTime now);
}
