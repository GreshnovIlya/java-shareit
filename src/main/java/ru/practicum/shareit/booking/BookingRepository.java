package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerOrderByStartDesc(User booker);

    List<Booking> findByBookerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(User booker, LocalDateTime now,
                                                                            LocalDateTime now1);

    List<Booking> findByBookerAndEndIsBeforeOrderByStartDesc(User booker, LocalDateTime now);

    List<Booking> findByBookerAndStartIsAfterOrderByStartDesc(User booker, LocalDateTime now);

    @Query(value = "Select * from bookings where booker_id=? and status=? order by start_time desc",
            nativeQuery = true)
    List<Booking> findByBookerAndStatusOrderByStartDesc(Long bookerId, String statusBooking);

    List<Booking> findByItemOwnerOrderByStartDesc(User owner);

    List<Booking> findByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(User owner, LocalDateTime now,
                                                                            LocalDateTime now1);

    List<Booking> findByItemOwnerAndEndIsBeforeOrderByStartDesc(User owner, LocalDateTime now);

    List<Booking> findByItemOwnerAndStartIsAfterOrderByStartDesc(User owner, LocalDateTime now);

    @Query(value = "Select b1_0.id,b1_0.booker_id,b1_0.end_time,b1_0.item_id,b1_0.start_time,b1_0.status from" +
            " bookings b1_0 left join items i1_0 on i1_0.id=b1_0.item_id left join users o1_0 on" +
            " o1_0.id=i1_0.owner_id where o1_0.id=? and b1_0.status=? order by b1_0.start_time desc",
            nativeQuery = true)
    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, String statusBooking);

    @Query(value = "Select b1_0.id,b1_0.booker_id,b1_0.end_time,b1_0.item_id,b1_0.start_time,b1_0.status from" +
            " bookings b1_0 where b1_0.item_id=? and b1_0.status=? and b1_0.start_time>?" +
            " order by b1_0.start_time desc", nativeQuery = true)
    List<Booking> findByItemIdAndStatusAndStartIsAfterOrderByStartDesc(Long itemId, String statusBooking,
                                                                       LocalDateTime now);

    @Query(value = "Select b1_0.id,b1_0.booker_id,b1_0.end_time,b1_0.item_id,b1_0.start_time,b1_0.status from" +
            " bookings b1_0 where b1_0.item_id=? and b1_0.status=? and b1_0.booker_id=? and b1_0.end_time<?" +
            " order by b1_0.start_time desc", nativeQuery = true)
    List<Booking> findByItemIdAndStatusAndBookerIdAndEndIsBeforeOrderByStartDesc(Long itemId, String statusBooking,
                                                                       Long bookerId, LocalDateTime now);
}
