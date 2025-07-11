package ru.practicum.shareit;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
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
@SpringJUnitConfig({ShareItApp.class, ItemServiceImpl.class, UserServiceImpl.class, ItemRequestServiceImpl.class})
public class ItemRequestServiceImplTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;
    private final ItemRequestService itemRequestService;

    @Test
    void testCreateRequest() {
        User requester = makeUser("Антон", "anton@email.com");
        ItemRequestDto newItemRequestDto = new ItemRequestDto(null, "Может забивать гвозди",
                LocalDateTime.now(), UserMapper.toUserDto(requester), null);
        TypedQuery<ItemRequest> query = em.createQuery("Select i from ItemRequest i where i.id = :id",
                ItemRequest.class);

        ItemRequestDto itemRequestDto = itemRequestService.create(newItemRequestDto, requester.getId());
        ItemRequest itemRequest = query.setParameter("id", itemRequestDto.getId())
                .getSingleResult();

        assertThat(itemRequest.getId(), equalTo(itemRequestDto.getId()));
        assertThat(itemRequest.getDescription(), equalTo(itemRequestDto.getDescription()));
        assertThat(itemRequest.getCreated(), equalTo(itemRequestDto.getCreated()));
        assertThat(itemRequest.getRequester(), equalTo(requester));
    }

    @Test
    void testGetByRequesterId() {
        User requester = makeUser("Антон", "anton@email.com");
        ItemRequestDto newItemRequestDto1 = new ItemRequestDto(null, "Может забивать гвозди",
                LocalDateTime.now(), UserMapper.toUserDto(requester), null);
        ItemRequestDto newItemRequestDto2 = new ItemRequestDto(null, "Может поливать грядки",
                LocalDateTime.now(), UserMapper.toUserDto(requester), null);
        TypedQuery<ItemRequest> query = em.createQuery("Select i from ItemRequest i where i.requester = :requester" +
                        " Order by i.created desc", ItemRequest.class);

        itemRequestService.create(newItemRequestDto1, requester.getId());
        itemRequestService.create(newItemRequestDto2, requester.getId());
        List<ItemRequestDto> itemRequests = itemRequestService.getByRequesterId(requester.getId());
        List<ItemRequest> requests = query.setParameter("requester", requester)
                .getResultList();

        assertThat(requests.size(), equalTo(itemRequests.size()));
        assertThat(requests.getFirst().getDescription(), equalTo(itemRequests.getFirst().getDescription()));
        assertThat(requests.getLast().getDescription(), equalTo(itemRequests.getLast().getDescription()));
    }

    @Test
    void testGetOthers() {
        User requester = makeUser("Антон", "anton@email.com");
        User user = makeUser("Игорь", "igor@email.com");
        ItemRequestDto newItemRequestDto1 = new ItemRequestDto(null, "Может забивать гвозди",
                LocalDateTime.now(), UserMapper.toUserDto(requester), null);
        ItemRequestDto newItemRequestDto2 = new ItemRequestDto(null, "Может поливать грядки",
                LocalDateTime.now(), UserMapper.toUserDto(requester), null);
        TypedQuery<ItemRequest> query = em.createQuery("Select i from ItemRequest i" +
                        " where i.requester != :requester Order by i.created desc", ItemRequest.class);

        itemRequestService.create(newItemRequestDto1, requester.getId());
        itemRequestService.create(newItemRequestDto2, requester.getId());
        List<ItemRequestDto> itemRequests = itemRequestService.getOthers(user.getId());
        List<ItemRequest> requests = query.setParameter("requester", user)
                .getResultList();

        assertThat(requests.size(), equalTo(itemRequests.size()));
        assertThat(requests.getFirst().getDescription(), equalTo(itemRequests.getFirst().getDescription()));
        assertThat(requests.getLast().getDescription(), equalTo(itemRequests.getLast().getDescription()));
    }

    @Test
    void testGetById() {
        User requester = makeUser("Антон", "anton@email.com");
        ItemRequestDto newItemRequestDto = new ItemRequestDto(null, "Может забивать гвозди",
                LocalDateTime.now(), UserMapper.toUserDto(requester), null);
        TypedQuery<ItemRequest> query = em.createQuery("Select i from ItemRequest i where i.id = :id" +
                " Order by i.created desc", ItemRequest.class);

        ItemRequestDto itemRequestDto = itemRequestService.create(newItemRequestDto, requester.getId());
        itemRequestDto = itemRequestService.getById(itemRequestDto.getId());
        ItemRequest itemRequest = query.setParameter("id", itemRequestDto.getId())
                .getSingleResult();

        assertThat(itemRequest.getId(), equalTo(itemRequestDto.getId()));
        assertThat(itemRequest.getDescription(), equalTo(itemRequestDto.getDescription()));
        assertThat(itemRequest.getCreated(), equalTo(itemRequestDto.getCreated()));
        assertThat(itemRequest.getRequester(), equalTo(requester));
    }


    private User makeUser(String name, String email) {
        UserDto userDto = new UserDto(null, name, email);
        userService.create(userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        return query.setParameter("email", userDto.getEmail())
                .getSingleResult();
    }
}
