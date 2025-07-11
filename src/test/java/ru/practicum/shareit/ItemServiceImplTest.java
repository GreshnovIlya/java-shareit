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
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = { "jdbc.url=jdbc:postgresql://localhost:5432/test"})
@SpringJUnitConfig({ShareItApp.class, ItemServiceImpl.class, UserServiceImpl.class, BookingServiceImpl.class})
public class ItemServiceImplTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

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
    void testCreateCommit() {
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

    private User makeUser(String name, String email) {
        UserDto userDto = new UserDto(null, name, email);
        userService.create(userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        return query.setParameter("email", userDto.getEmail())
                .getSingleResult();
    }
}
