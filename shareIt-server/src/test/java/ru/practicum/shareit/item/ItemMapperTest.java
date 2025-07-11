package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemAnswerDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ItemMapperTest {
    @Test
    void testToItemAnswerDto() {
        Item item = new Item(1L, new User(1L, "Егор", "email@yandex.ru"), "Чайник",
                "Кипятит воду", true, null);

        ItemAnswerDto itemAnswerDto = ItemMapper.toItemAnswerDto(item);

        assertThat(itemAnswerDto.getId(), equalTo(1L));
        assertThat(itemAnswerDto.getName(), equalTo("Чайник"));
        assertThat(itemAnswerDto.getOwnerId(), equalTo(1L));
    }
}
