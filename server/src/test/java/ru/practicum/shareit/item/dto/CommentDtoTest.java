package ru.practicum.shareit.item.dto;

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
public class CommentDtoTest {
    private final JacksonTester<CommentDto> json;

    LocalDateTime localDateTime =
            LocalDateTime.of(2100, Month.DECEMBER, 12, 12, 12, 12);

    @Test
    void testCommentDto() throws Exception {
        CommentDto commentDto = new CommentDto(1L, "Нормальный", "Егор", localDateTime);

        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Нормальный");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("Егор");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(localDateTime.toString());
    }
}
