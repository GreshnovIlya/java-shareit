package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemAnswerDtoTest {
    private final JacksonTester<ItemAnswerDto> json;

    @Test
    void testItemAnswerDto() throws Exception {
        ItemAnswerDto itemDto = new ItemAnswerDto(1L, "Молоток", 1L);

        JsonContent<ItemAnswerDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Молоток");
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(1);
    }
}
