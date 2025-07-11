package ru.practicum.shareit;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = { "jdbc.url=jdbc:postgresql://localhost:5432/test"})
@SpringJUnitConfig({ShareItApp.class, ItemServiceImpl.class, UserServiceImpl.class, BookingServiceImpl.class})
public class BookingServiceImplTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    @Test
    void testCreateBooking() {
        User owner = makeUser("Антон", "anton@email.com");
        User booker = makeUser("Игорь", "igor@email.com");
        Item item = makeItem(owner.getId());
        NewBookingDto newBookingDto = new NewBookingDto(item.getId(), LocalDateTime.now(), LocalDateTime.now());
        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);

        BookingDto bookingDto = bookingService.create(newBookingDto, booker.getId());
        Booking booking = query.setParameter("id", bookingDto.getId())
                .getSingleResult();

        assertThat(booking.getId(), equalTo(bookingDto.getId()));
        assertThat(booking.getStart(), equalTo(bookingDto.getStart()));
        assertThat(booking.getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(booking.getItem(), equalTo(item));
        assertThat(booking.getBooker(), equalTo(booker));
        assertThat(booking.getStatus(), equalTo(StatusBooking.WAITING));
    }

    @Test
    void testConfirmationBooking() {
        User owner = makeUser("Антон", "anton@email.com");
        User booker = makeUser("Игорь", "igor@email.com");
        Item item = makeItem(owner.getId());
        NewBookingDto newBookingDto = new NewBookingDto(item.getId(), LocalDateTime.now(), LocalDateTime.now());
        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);

        BookingDto bookingDto = bookingService.create(newBookingDto, booker.getId());
        bookingService.confirmation(owner.getId(), bookingDto.getId(), true);
        Booking booking = query.setParameter("id", bookingDto.getId())
                .getSingleResult();

        assertThat(booking.getStatus(), equalTo(StatusBooking.APPROVED));
    }

    @Test
    void testGetBooking() {
        User owner = makeUser("Антон", "anton@email.com");
        User booker = makeUser("Игорь", "igor@email.com");
        Item item = makeItem(owner.getId());
        NewBookingDto newBookingDto = new NewBookingDto(item.getId(), LocalDateTime.now(), LocalDateTime.now());
        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);

        BookingDto bookingDto = bookingService.create(newBookingDto, booker.getId());
        bookingDto = bookingService.get(booker.getId(), bookingDto.getId());
        Booking booking = query.setParameter("id", bookingDto.getId())
                .getSingleResult();

        assertThat(bookingDto.getId(), equalTo(booking.getId()));
        assertThat(bookingDto.getStart(), equalTo(booking.getStart()));
        assertThat(bookingDto.getEnd(), equalTo(booking.getEnd()));
        assertThat(bookingDto.getItem(), equalTo(ItemMapper.toItemDto(item)));
        assertThat(bookingDto.getBooker(), equalTo(UserMapper.toUserDto(booker)));
        assertThat(bookingDto.getStatus(), equalTo(booking.getStatus()));
    }

    @Test
    void testGetAllBookingUser() {
        User owner = makeUser("Антон", "anton@email.com");
        User booker = makeUser("Игорь", "igor@email.com");
        Item item1 = makeItem(owner.getId());
        Item item2 = makeItem(owner.getId());
        NewBookingDto newBookingDto1 = new NewBookingDto(item1.getId(), LocalDateTime.now(), LocalDateTime.now());
        NewBookingDto newBookingDto2 = new NewBookingDto(item2.getId(), LocalDateTime.now(), LocalDateTime.now());

        BookingDto bookingDto1 = bookingService.create(newBookingDto1, booker.getId());
        BookingDto bookingDto2 = bookingService.create(newBookingDto2, booker.getId());
        bookingService.confirmation(owner.getId(), bookingDto1.getId(), true);
        bookingService.confirmation(owner.getId(), bookingDto2.getId(), true);
        List<BookingDto> bookings = bookingService.getAllBookingUser(booker.getId(), State.ALL);

        assertThat(bookings.size(), equalTo(2));
    }

    @Test
    void testGetAllBookingOwner() {
        User owner = makeUser("Антон", "anton@email.com");
        User booker = makeUser("Игорь", "igor@email.com");
        Item item1 = makeItem(owner.getId());
        Item item2 = makeItem(owner.getId());
        NewBookingDto newBookingDto1 = new NewBookingDto(item1.getId(), LocalDateTime.now(), LocalDateTime.now());
        NewBookingDto newBookingDto2 = new NewBookingDto(item2.getId(), LocalDateTime.now(), LocalDateTime.now());

        BookingDto bookingDto1 = bookingService.create(newBookingDto1, booker.getId());
        BookingDto bookingDto2 = bookingService.create(newBookingDto2, booker.getId());
        bookingService.confirmation(owner.getId(), bookingDto1.getId(), true);
        bookingService.confirmation(owner.getId(), bookingDto2.getId(), true);
        List<BookingDto> bookings = bookingService.getAllBookingOwner(owner.getId(), State.ALL);

        assertThat(bookings.size(), equalTo(2));
    }

    private User makeUser(String name, String email) {
        UserDto userDto = new UserDto(null, name, email);
        userService.create(userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        return query.setParameter("email", userDto.getEmail())
                .getSingleResult();
    }

    private Item makeItem(Long ownerId) {
        NewItemDto newItemDto =
                new NewItemDto(null, "Чайник", "Кипятит воду", true, null);
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);

        ItemDto itemDto = itemService.create(newItemDto, ownerId);
        return query.setParameter("id", itemDto.getId())
                .getSingleResult();
    }
}
