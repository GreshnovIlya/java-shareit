package ru.practicum.shareit.user;

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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.exception.EmailExistException;
import ru.practicum.shareit.exception.ErrorHandler;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = { "jdbc.url=jdbc:postgresql://localhost:5432/test"})
@SpringJUnitConfig({ShareItServer.class, UserServiceImpl.class, ErrorHandler.class})
class UserServiceImplTest {

    private final EntityManager em;
    private final UserService userService;
    private final ErrorHandler errorHandler;

    @Test
    void testCrateUser() {
        UserDto userDto = new UserDto(null, "Антон", "anton@email.com");
        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);

        userService.create(userDto);
        User user = query.setParameter("email", userDto.getEmail())
                .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
        assertThat(user.getName(), equalTo(userDto.getName()));
    }

    @Test
    void failTestCreateUserEmailExist() {
        UserDto userDto = new UserDto(null, "Антон", "anton@email.com");
        UserDto userDto1 = new UserDto(null, "Антон", "anton@email.com");
        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);

        userService.create(userDto);
        errorHandler.handleEmailValidation(Assertions.assertThrows(EmailExistException.class, () -> {
            userService.create(userDto1);
        }, String.format("Почта %s уже существует или неверно указана", userDto1.getEmail())));
    }

    @Test
    void testUpdateUser() {
        UserDto userDto = new UserDto(null, "Игорь", "igor@email.com");
        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);

        userService.create(userDto);
        User user = query.setParameter("email", userDto.getEmail())
                .getSingleResult();

        UserDto updateUserDto = new UserDto(null, "Игорян", "igorian@email.com");
        userService.update(user.getId(), updateUserDto);

        User updateUser = query.setParameter("email", updateUserDto.getEmail())
                .getSingleResult();

        assertThat(updateUser.getId(), notNullValue());
        assertThat(updateUser.getEmail(), equalTo(updateUserDto.getEmail()));
        assertThat(updateUser.getName(), equalTo(updateUserDto.getName()));
    }

    @Test
    void testGetUser() {
        UserDto userDto = new UserDto(null, "Игорь", "igor@email.com");
        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);

        userService.create(userDto);
        User user = query.setParameter("email", userDto.getEmail())
                .getSingleResult();

        UserDto userInData = userService.get(user.getId());

        assertThat(userInData.getId(), notNullValue());
        assertThat(userInData.getEmail(), equalTo(userDto.getEmail()));
        assertThat(userInData.getName(), equalTo(userDto.getName()));
    }

    @Test
    void testGetAllUser() {
        UserDto userDto1 = new UserDto(null, "Игорь", "igor@email.com");
        userService.create(userDto1);
        UserDto userDto2 = new UserDto(null, "Константин", "kostia@email.com");
        userService.create(userDto2);

        List<UserDto> users = userService.getAll();

        assertThat((long) users.size(), equalTo(2L));
    }

    @Test
    void testRemoveUser() {
        UserDto userDto = new UserDto(null, "Игорь", "igor@email.com");
        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);

        userService.create(userDto);
        User user = query.setParameter("email", userDto.getEmail())
                .getSingleResult();

        userService.remove(user.getId());

        assertThat(userService.getAll(), equalTo(List.of()));
    }
}
