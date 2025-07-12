package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingDtoTest {
    private final JacksonTester<BookingDto> json;

    LocalDateTime localDateTime =
            LocalDateTime.of(2100, Month.DECEMBER, 12, 12, 12, 12);

    @Test
    void testBookingDto() throws Exception {
        BookingDto bookingDto = new BookingDto(1L, localDateTime, localDateTime,
                new ItemDto(1L, "Чайник", "Кипятит", true, null),
                new UserDto(1L, "Егор", "igor@yandex.ru"), StatusBooking.WAITING);

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(localDateTime.toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(localDateTime.toString());
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("Чайник");
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo("Кипятит");
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(true);
        assertThat(result).extractingJsonPathStringValue("$.item.request").isEqualTo(null);
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("Егор");
        assertThat(result).extractingJsonPathStringValue("$.booker.email").isEqualTo("igor@yandex.ru");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }
}
