package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ErrorHandler;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = { "jdbc.url=jdbc:postgresql://localhost:5432/test"})
@SpringJUnitConfig({ShareItServer.class, ItemServiceImpl.class, UserServiceImpl.class, BookingServiceImpl.class,
        ItemRequestServiceImpl.class, ErrorHandler.class})
public class ItemServiceImplTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private final ItemRequestService itemRequestService;
    private final ErrorHandler errorHandler;

    @Test
    void testSaveItem() {
        User user = makeUser("Антон", "anton@email.com");
        NewItemDto newItemDto =
                new NewItemDto(null, "Чайник", "Кипятит воду", true, null);
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);

        ItemDto itemDto = itemService.create(newItemDto, user.getId());
        Item item = query.setParameter("id", itemDto.getId())
                .getSingleResult();

        assertThat(item.getId(), equalTo(itemDto.getId()));
        assertThat(item.getName(), equalTo(newItemDto.getName()));
        assertThat(item.getDescription(), equalTo(newItemDto.getDescription()));
        assertThat(item.getOwner(), equalTo(user));
        assertThat(item.getAvailable(), equalTo(newItemDto.getAvailable()));
    }

    @Test
    void errorTestSaveItemNotUser() {
        User user = makeUser("Антон", "anton@email.com");
        NewItemDto newItemDto =
                new NewItemDto(null, "Чайник", "Кипятит воду", true, null);

        Assertions.assertThrows(NotFoundException.class, () -> {
            itemService.create(newItemDto, user.getId() + 1);
        }, String.format("Пользователь с id = %s не найден", user.getId() + 1));
    }

    @Test
    void testSaveItemThroughRequest() {
        UserDto userDto = new UserDto(null, "Имя", "email@iandex.ru");
        userDto = userService.create(userDto);
        ItemRequestDto newItemRequestDto = new ItemRequestDto(null, "Может забивать гвозди",
                LocalDateTime.now(), userDto, null);
        newItemRequestDto = itemRequestService.create(newItemRequestDto, userDto.getId());
        User user = makeUser("Антон", "anton@email.com");
        NewItemDto newItemDto =
                new NewItemDto(null, "Чайник", "Кипятит воду", true,
                        newItemRequestDto.getId());
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);

        ItemDto itemDto = itemService.create(newItemDto, user.getId());
        Item item = query.setParameter("id", itemDto.getId())
                .getSingleResult();

        assertThat(item.getId(), equalTo(itemDto.getId()));
        assertThat(item.getName(), equalTo(newItemDto.getName()));
        assertThat(item.getDescription(), equalTo(newItemDto.getDescription()));
        assertThat(item.getOwner(), equalTo(user));
        assertThat(item.getAvailable(), equalTo(newItemDto.getAvailable()));
    }

    @Test
    void errorTestSaveItemThroughRequestNotRequest() {
        UserDto userDto = new UserDto(null, "Имя", "email@iandex.ru");
        userDto = userService.create(userDto);
        ItemRequestDto newItemRequestDto = new ItemRequestDto(null, "Может забивать гвозди",
                LocalDateTime.now(), userDto, null);
        newItemRequestDto = itemRequestService.create(newItemRequestDto, userDto.getId());
        User user = makeUser("Антон", "anton@email.com");
        NewItemDto newItemDto =
                new NewItemDto(null, "Чайник", "Кипятит воду", true,
                        newItemRequestDto.getId() + 1);
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);

        Assertions.assertThrows(NotFoundException.class, () -> {
            ItemDto itemDto = itemService.create(newItemDto, user.getId());
        }, String.format("Запрос с id = %s не найден", newItemRequestDto.getId() + 1));
    }

    @Test
    void errorTestSaveItemThroughRequestNotUser() {
        User user = makeUser("Антон", "anton@email.com");
        NewItemDto newItemDto =
                new NewItemDto(null, "Чайник", "Кипятит воду", true, 1L);
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);

        Assertions.assertThrows(NotFoundException.class, () -> {
            ItemDto itemDto = itemService.create(newItemDto, user.getId() + 1);
        }, String.format("Пользователь с id = %s не найден", user.getId() + 1));
    }

    @Test
    void testUpdateItem() {
        User user = makeUser("Антон", "anton@email.com");
        NewItemDto newItemDto =
                new NewItemDto(null, "Чайник", "Кипятит воду", true, null);
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);
        ItemDto updateItemDto =
                new ItemDto(null, "Молоток", "Молотит", true, null);

        ItemDto itemDto = itemService.create(newItemDto, user.getId());
        Item item = query.setParameter("id", itemDto.getId())
                .getSingleResult();
        updateItemDto = itemService.update(updateItemDto, user.getId(), item.getId());
        Item updateItem = query.setParameter("id", updateItemDto.getId())
                .getSingleResult();

        assertThat(updateItem.getId(), equalTo(updateItemDto.getId()));
        assertThat(updateItem.getName(), equalTo(updateItemDto.getName()));
        assertThat(updateItem.getDescription(), equalTo(updateItemDto.getDescription()));
        assertThat(updateItem.getOwner(), equalTo(user));
        assertThat(updateItem.getAvailable(), equalTo(updateItemDto.getAvailable()));
    }

    @Test
    void errorTestUpdateItemNotUser() {
        User user = makeUser("Антон", "anton@email.com");
        NewItemDto newItemDto =
                new NewItemDto(null, "Чайник", "Кипятит воду", true, null);
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);
        ItemDto updateItemDto =
                new ItemDto(null, "Молоток", "Молотит", true, null);

        ItemDto itemDto = itemService.create(newItemDto, user.getId());
        Item item = query.setParameter("id", itemDto.getId())
                .getSingleResult();

        Assertions.assertThrows(NotFoundException.class, () -> {
            itemService.update(updateItemDto, user.getId() + 1, item.getId());
        }, String.format("Пользователь с id = %s не найден", user.getId() + 1));
    }

    @Test
    void errorTestUpdateItemNotOwner() {
        User user = makeUser("Антон", "anton@email.com");
        User user1 = makeUser("Игорь", "igor@email.com");
        NewItemDto newItemDto =
                new NewItemDto(null, "Чайник", "Кипятит воду", true, null);
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);
        ItemDto updateItemDto =
                new ItemDto(null, "Молоток", "Молотит", true, null);

        ItemDto itemDto = itemService.create(newItemDto, user.getId());
        Item item = query.setParameter("id", itemDto.getId())
                .getSingleResult();

        errorHandler.handleValidation(Assertions.assertThrows(BadRequestException.class, () -> {
            itemService.update(updateItemDto, user1.getId(), item.getId());
        }, "Пользователь с id = %s не владеет данной вещью"));
    }

    @Test
    void testGetItem() {
        User user = makeUser("Антон", "anton@email.com");
        NewItemDto newItemDto =
                new NewItemDto(null, "Чайник", "Кипятит воду", true, null);
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);

        ItemDto itemDto = itemService.create(newItemDto, user.getId());
        ItemCommentDto itemCommentDto = itemService.get(itemDto.getId());
        Item item = query.setParameter("id", itemDto.getId())
                .getSingleResult();

        assertThat(itemCommentDto.getId(), equalTo(item.getId()));
        assertThat(itemCommentDto.getName(), equalTo(item.getName()));
        assertThat(itemCommentDto.getDescription(), equalTo(item.getDescription()));
        assertThat(itemCommentDto.getAvailable(), equalTo(item.getAvailable()));
    }

    @Test
    void testGetItemWithRequest() {
        User owner = makeUser("Антон", "anton@email.com");
        User requester = makeUser("Игорь", "igor@email.com");
        ItemRequestDto newItemRequestDto = new ItemRequestDto(null, "Может забивать гвозди",
                LocalDateTime.now(), UserMapper.toUserDto(requester), null);
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);

        ItemRequestDto itemRequestDto = itemRequestService.create(newItemRequestDto, requester.getId());
        NewItemDto newItemDto =
                new NewItemDto(null, "Чайник", "Кипятит воду", true, itemRequestDto.getId());
        ItemDto itemDto = itemService.create(newItemDto, owner.getId());
        ItemCommentDto itemCommentDto = itemService.get(itemDto.getId());
        Item item = query.setParameter("id", itemDto.getId())
                .getSingleResult();

        assertThat(itemCommentDto.getId(), equalTo(item.getId()));
        assertThat(itemCommentDto.getName(), equalTo(item.getName()));
        assertThat(itemCommentDto.getDescription(), equalTo(item.getDescription()));
        assertThat(itemCommentDto.getAvailable(), equalTo(item.getAvailable()));
        assertThat(itemCommentDto.getRequest(), equalTo(new ItemRequestInItemDto(itemRequestDto.getId(),
                itemRequestDto.getDescription(), itemRequestDto.getCreated(), itemRequestDto.getRequester())));
    }

    @Test
    void testGetItemWithNextAndLastBooking() {
        LocalDateTime localDateTime =
                LocalDateTime.of(2100, Month.DECEMBER, 12, 12, 12, 12);
        User owner = makeUser("Антон", "anton@email.com");
        User booker = makeUser("Игорь", "igor@email.com");
        NewItemDto newItemDto =
                new NewItemDto(null, "Чайник", "Кипятит воду", true, null);
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);

        ItemDto itemDto = itemService.create(newItemDto, owner.getId());
        NewBookingDto newBookingDto = new NewBookingDto(itemDto.getId(), localDateTime, localDateTime);
        NewBookingDto newBookingDto1 = new NewBookingDto(itemDto.getId(), localDateTime.plusDays(1), localDateTime.plusDays(1));
        BookingDto bookingDto = bookingService.create(newBookingDto, booker.getId());
        bookingService.confirmation(owner.getId(), bookingDto.getId(), true);
        BookingDto bookingDto1 = bookingService.create(newBookingDto1, booker.getId());
        bookingService.confirmation(owner.getId(), bookingDto1.getId(), true);
        ItemCommentDto itemCommentDto = itemService.get(itemDto.getId());
        Item item = query.setParameter("id", itemDto.getId())
                .getSingleResult();

        assertThat(itemCommentDto.getId(), equalTo(item.getId()));
        assertThat(itemCommentDto.getName(), equalTo(item.getName()));
        assertThat(itemCommentDto.getDescription(), equalTo(item.getDescription()));
        assertThat(itemCommentDto.getAvailable(), equalTo(item.getAvailable()));
        assertThat(itemCommentDto.getLastBooking(), equalTo(localDateTime.plusDays(1)));
        assertThat(itemCommentDto.getNextBooking(), equalTo(localDateTime));
    }

    @Test
    void errorTestGetItemNotItem() {
        User user = makeUser("Антон", "anton@email.com");
        NewItemDto newItemDto =
                new NewItemDto(null, "Чайник", "Кипятит воду", true, null);

        ItemDto itemDto = itemService.create(newItemDto, user.getId());

        Assertions.assertThrows(NotFoundException.class, () -> {
            itemService.get(itemDto.getId() + 1);
        }, String.format("Вещь с id = %s не найдена", itemDto.getId() + 1));
    }

    @Test
    void testGetAllItemByOwnerItem() {
        User user = makeUser("Антон", "anton@email.com");
        NewItemDto newItemDto =
                new NewItemDto(null, "Чайник", "Кипятит воду", true, null);
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);

        ItemDto itemDto = itemService.create(newItemDto, user.getId());
        List<ItemCommentDto> items = itemService.getAllItemByOwner(user.getId());
        ItemCommentDto itemCommentDto = items.getFirst();
        List<Item> itemsInDate = query.setParameter("id", itemDto.getId())
                .getResultList();
        Item itemInDate = itemsInDate.getFirst();

        assertThat(items.size(), equalTo(itemsInDate.size()));
        assertThat(itemCommentDto.getName(), equalTo(itemInDate.getName()));
        assertThat(itemCommentDto.getDescription(), equalTo(itemInDate.getDescription()));
        assertThat(itemCommentDto.getAvailable(), equalTo(itemInDate.getAvailable()));
    }

    @Test
    void testGetAllItemByOwnerItemWithNextAndLastBooking() {
        LocalDateTime localDateTime =
                LocalDateTime.of(2100, Month.DECEMBER, 12, 12, 12, 12);
        User owner = makeUser("Антон", "anton@email.com");
        User booker = makeUser("Игорь", "igor@email.com");
        NewItemDto newItemDto =
                new NewItemDto(null, "Чайник", "Кипятит воду", true, null);
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);

        ItemDto itemDto = itemService.create(newItemDto, owner.getId());
        NewBookingDto newBookingDto = new NewBookingDto(itemDto.getId(), localDateTime, localDateTime);
        NewBookingDto newBookingDto1 = new NewBookingDto(itemDto.getId(), localDateTime.plusDays(1), localDateTime.plusDays(1));
        BookingDto bookingDto = bookingService.create(newBookingDto, booker.getId());
        bookingService.confirmation(owner.getId(), bookingDto.getId(), true);
        BookingDto bookingDto1 = bookingService.create(newBookingDto1, booker.getId());
        bookingService.confirmation(owner.getId(), bookingDto1.getId(), true);
        List<ItemCommentDto> items = itemService.getAllItemByOwner(owner.getId());
        ItemCommentDto itemCommentDto = items.getFirst();
        List<Item> itemsInDate = query.setParameter("id", itemDto.getId())
                .getResultList();
        Item itemInDate = itemsInDate.getFirst();

        assertThat(items.size(), equalTo(itemsInDate.size()));
        assertThat(itemCommentDto.getName(), equalTo(itemInDate.getName()));
        assertThat(itemCommentDto.getDescription(), equalTo(itemInDate.getDescription()));
        assertThat(itemCommentDto.getAvailable(), equalTo(itemInDate.getAvailable()));
        assertThat(itemCommentDto.getLastBooking(), equalTo(localDateTime.plusDays(1)));
        assertThat(itemCommentDto.getNextBooking(), equalTo(localDateTime));
    }

    @Test
    void errorTestGetAllItemByOwnerItemNotUser() {
        User user = makeUser("Антон", "anton@email.com");
        NewItemDto newItemDto =
                new NewItemDto(null, "Чайник", "Кипятит воду", true, null);

        itemService.create(newItemDto, user.getId());

        Assertions.assertThrows(NotFoundException.class, () -> {
            itemService.getAllItemByOwner(user.getId() + 1);
        }, String.format("Пользователь с id = %s не найден", user.getId() + 1));
    }

    @Test
    void testGetItemsByNameOrDescription() {
        User user = makeUser("Антон", "anton@email.com");
        NewItemDto newItemDto1 =
                new NewItemDto(null, "Чайник", "Кипятит воду", true, null);
        NewItemDto newItemDto2 =
                new NewItemDto(null, "Молоток", "Молотит", true, null);
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id in :id", Item.class);

        ItemDto itemDto1 = itemService.create(newItemDto1, user.getId());
        itemService.create(newItemDto2, user.getId());
        List<ItemDto> items = itemService.getItemsByNameOrDescription("ча");
        ItemDto itemCommentDto = items.getFirst();
        List<Item> itemsInDate = query.setParameter("id", List.of(itemDto1.getId()))
                .getResultList();
        Item itemInDate = itemsInDate.getFirst();

        assertThat(items.size(), equalTo(itemsInDate.size()));
        assertThat(itemCommentDto.getName(), equalTo(itemInDate.getName()));
        assertThat(itemCommentDto.getDescription(), equalTo(itemInDate.getDescription()));
        assertThat(itemCommentDto.getAvailable(), equalTo(itemInDate.getAvailable()));
    }

    @Test
    void testCreateCommit() throws InterruptedException {
        User owner = makeUser("Антон", "anton@email.com");
        User author = makeUser("Игорь", "igor@email.com");
        NewItemDto newItemDto =
                new NewItemDto(null, "Чайник", "Кипятит воду", true, null);
        TypedQuery<Comment> queryCommit = em.createQuery("Select c from Comment c where c.id = :id", Comment.class);
        TypedQuery<Item> queryItem = em.createQuery("Select i from Item i where i.id in :id", Item.class);

        ItemDto itemDto = itemService.create(newItemDto, owner.getId());
        NewBookingDto newBookingDto =
                new NewBookingDto(itemDto.getId(), LocalDateTime.now(), LocalDateTime.now());
        BookingDto bookingDto = bookingService.create(newBookingDto, author.getId());
        bookingService.confirmation(owner.getId(), bookingDto.getId(), true);
        TimeUnit.SECONDS.sleep(1);
        CommentDto commentDto = new CommentDto(null, "Все супер", author.getName(), LocalDateTime.now());
        commentDto = itemService.createComment(commentDto, author.getId(), itemDto.getId());
        Comment comment = queryCommit.setParameter("id", commentDto.getId())
                .getSingleResult();
        Item item = queryItem.setParameter("id", itemDto.getId())
                .getSingleResult();


        assertThat(comment.getId(), notNullValue());
        assertThat(comment.getText(), equalTo(commentDto.getText()));
        assertThat(comment.getCreated(), equalTo(commentDto.getCreated()));
        assertThat(comment.getAuthor().getName(), equalTo(commentDto.getAuthorName()));
        assertThat(comment.getItem(), equalTo(item));
    }

    @Test
    void errorTestCreateCommitNotUser() throws InterruptedException {
        User owner = makeUser("Антон", "anton@email.com");
        User author = makeUser("Игорь", "igor@email.com");
        NewItemDto newItemDto =
                new NewItemDto(null, "Чайник", "Кипятит воду", true, null);

        ItemDto itemDto = itemService.create(newItemDto, owner.getId());
        NewBookingDto newBookingDto =
                new NewBookingDto(itemDto.getId(), LocalDateTime.now(), LocalDateTime.now());
        BookingDto bookingDto = bookingService.create(newBookingDto, author.getId());
        bookingService.confirmation(owner.getId(), bookingDto.getId(), true);
        TimeUnit.SECONDS.sleep(1);
        CommentDto commentDto = new CommentDto(null, "Все супер", author.getName(), LocalDateTime.now());

        Assertions.assertThrows(NotFoundException.class, () -> {
            itemService.createComment(commentDto, author.getId() + 1, itemDto.getId());
        }, String.format("Пользователь с id = %s не найден", author.getId() + 1));
    }

    @Test
    void errorTestCreateCommitNotBooker() throws InterruptedException {
        User owner = makeUser("Антон", "anton@email.com");
        User author = makeUser("Игорь", "igor@email.com");
        User user = makeUser("Инна", "inna@email.com");
        NewItemDto newItemDto =
                new NewItemDto(null, "Чайник", "Кипятит воду", true, null);

        ItemDto itemDto = itemService.create(newItemDto, owner.getId());
        NewBookingDto newBookingDto =
                new NewBookingDto(itemDto.getId(), LocalDateTime.now(), LocalDateTime.now());
        BookingDto bookingDto = bookingService.create(newBookingDto, author.getId());
        bookingService.confirmation(owner.getId(), bookingDto.getId(), true);
        TimeUnit.SECONDS.sleep(1);
        CommentDto commentDto = new CommentDto(null, "Все супер", author.getName(), LocalDateTime.now());

        Assertions.assertThrows(BadRequestException.class, () -> {
            itemService.createComment(commentDto, user.getId(), itemDto.getId());
        }, String.format("Пользователь с id = %s не брал в аренду эту вещь", user.getId()));
    }

    private User makeUser(String name, String email) {
        UserDto userDto = new UserDto(null, name, email);
        userService.create(userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        return query.setParameter("email", userDto.getEmail())
                .getSingleResult();
    }
}
