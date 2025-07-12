package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;

    @Autowired
    private MockMvc mvc;

    LocalDateTime localDateTime =
            LocalDateTime.of(2100, Month.DECEMBER, 12, 12, 12, 12);
    ItemCommentDto itemCommentDto = new ItemCommentDto(1L, "Чайник", "Кипятит воду", true,
            null, localDateTime, localDateTime, null);
    ItemDto itemDto = new ItemDto(1L, "Чайник", "Кипятит воду", true, null);

    @Test
    void createItem() throws Exception {
        NewItemDto newItemDto =
                new NewItemDto(1L, "Чайник", "Кипятит воду", true, null);

        when(itemService.create(any(), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(newItemDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.request", is(itemDto.getRequest())));
    }

    @Test
    void updateItem() throws Exception {
        ItemDto updateItemDto = new ItemDto(1L, "Молоток", "Молотит", true, null);

        when(itemService.update(any(), anyLong(), anyLong()))
                .thenReturn(updateItemDto);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(updateItemDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(updateItemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(updateItemDto.getName())))
                .andExpect(jsonPath("$.description", is(updateItemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(updateItemDto.getAvailable())))
                .andExpect(jsonPath("$.request", is(updateItemDto.getRequest())));
    }

    @Test
    void getItem() throws Exception {
        when(itemService.get(anyLong()))
                .thenReturn(itemCommentDto);

        mvc.perform(get("/items/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(itemCommentDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemCommentDto.getName())))
                .andExpect(jsonPath("$.description", is(itemCommentDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemCommentDto.getAvailable())))
                .andExpect(jsonPath("$.request", is(itemCommentDto.getRequest())))
                .andExpect(jsonPath("$.nextBooking", is(itemCommentDto.getNextBooking().toString())))
                .andExpect(jsonPath("$.lastBooking", is(itemCommentDto.getLastBooking().toString())))
                .andExpect(jsonPath("$.comments", is(itemCommentDto.getComments())));
    }

    @Test
    void getAllItemByOwner() throws Exception {
        when(itemService.getAllItemByOwner(anyLong()))
                .thenReturn(List.of(itemCommentDto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(itemCommentDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemCommentDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemCommentDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemCommentDto.getAvailable())))
                .andExpect(jsonPath("$[0].request", is(itemCommentDto.getRequest())))
                .andExpect(jsonPath("$[0].nextBooking", is(itemCommentDto.getNextBooking().toString())))
                .andExpect(jsonPath("$[0].lastBooking", is(itemCommentDto.getLastBooking().toString())))
                .andExpect(jsonPath("$[0].comments", is(itemCommentDto.getComments())));
    }

    @Test
    void getItemsByNameOrDescription() throws Exception {
        when(itemService.getItemsByNameOrDescription(anyString()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search?text={text}", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$[0].request", is(itemDto.getRequest())));
    }

    @Test
    void createComment() throws Exception {
        CommentDto commentDto =
                new CommentDto(1L, "Все хорошо", "Николай", localDateTime);

        when(itemService.createComment(any(), anyLong(), anyLong()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", 2)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
                .andExpect(jsonPath("$.created", is(commentDto.getCreated().toString())));
    }
}
