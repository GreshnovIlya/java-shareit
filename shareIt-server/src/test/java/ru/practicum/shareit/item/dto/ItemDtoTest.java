package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestInItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemDtoTest {
    private final JacksonTester<ItemDto> json;

    LocalDateTime localDateTime =
            LocalDateTime.of(2100, Month.DECEMBER, 12, 12, 12, 12);

    @Test
    void testItemDtoTest() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "Молоток", "Молотит", true,
                new ItemRequestInItemDto(1L, "Молоток до 2 кг", localDateTime,
                        new UserDto(1L, "Егор", "egor.yandex.ru")));

                JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Молоток");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Молотит");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.request.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.request.description").isEqualTo("Молоток до 2 кг");
        assertThat(result).extractingJsonPathStringValue("$.request.created").isEqualTo(localDateTime.toString());
        assertThat(result).extractingJsonPathNumberValue("$.request.requester.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.request.requester.name").isEqualTo("Егор");
        assertThat(result).extractingJsonPathStringValue("$.request.requester.email").isEqualTo("egor.yandex.ru");
    }
}

