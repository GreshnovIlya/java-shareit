package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class NewBookingDtoTest {
    private final JacksonTester<NewBookingDto> json;

    LocalDateTime localDateTime =
            LocalDateTime.of(2100, Month.DECEMBER, 12, 12, 12, 12);

    @Test
    void testNewBookingDto() throws Exception {
        NewBookingDto newBookingDto = new NewBookingDto(1L, localDateTime, localDateTime);

        JsonContent<NewBookingDto> result = json.write(newBookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(localDateTime.toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(localDateTime.toString());
    }
}
