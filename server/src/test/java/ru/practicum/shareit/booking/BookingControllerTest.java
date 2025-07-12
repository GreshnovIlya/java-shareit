package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    LocalDateTime localDateTime =
            LocalDateTime.of(2100, Month.DECEMBER, 12, 12, 12, 12);
    BookingDto bookingDto = new BookingDto(1L, localDateTime, localDateTime,
            new ItemDto(1L, "Чайник", "Кипятит воду", true, null),
            new UserDto(1L, "Егор", "igor@yandex.ru"), StatusBooking.WAITING);

    @Test
    void createBooking() throws Exception {
        NewBookingDto newBookingDto = new NewBookingDto(1L, localDateTime, localDateTime);

        when(bookingService.create(any(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(newBookingDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId().intValue())))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.item.description", is(bookingDto.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", is(bookingDto.getItem().getAvailable())))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId().intValue())))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", is(bookingDto.getBooker().getEmail())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void confirmationBookingTrue() throws Exception {
        BookingDto bookingDtoApproved = new BookingDto(1L, localDateTime, localDateTime,
                new ItemDto(1L, "Чайник", "Кипятит воду", true, null),
                new UserDto(1L, "Егор", "igor@yandex.ru"), StatusBooking.APPROVED);

        when(bookingService.confirmation(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDtoApproved);

        mvc.perform(patch("/bookings/1?approved={}", true)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(bookingDtoApproved.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDtoApproved.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDtoApproved.getEnd().toString())))
                .andExpect(jsonPath("$.item.id", is(bookingDtoApproved.getItem().getId().intValue())))
                .andExpect(jsonPath("$.item.name", is(bookingDtoApproved.getItem().getName())))
                .andExpect(jsonPath("$.item.description", is(bookingDtoApproved.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", is(bookingDtoApproved.getItem().getAvailable())))
                .andExpect(jsonPath("$.booker.id", is(bookingDtoApproved.getBooker().getId().intValue())))
                .andExpect(jsonPath("$.booker.name", is(bookingDtoApproved.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", is(bookingDtoApproved.getBooker().getEmail())))
                .andExpect(jsonPath("$.status", is(bookingDtoApproved.getStatus().toString())));
    }

    @Test
    void confirmationBookingFalse() throws Exception {
        BookingDto bookingDtoApproved = new BookingDto(1L, localDateTime, localDateTime,
                new ItemDto(1L, "Чайник", "Кипятит воду", true, null),
                new UserDto(1L, "Егор", "igor@yandex.ru"), StatusBooking.REJECTED);

        when(bookingService.confirmation(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDtoApproved);

        mvc.perform(patch("/bookings/1?approved={}", false)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(bookingDtoApproved.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDtoApproved.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDtoApproved.getEnd().toString())))
                .andExpect(jsonPath("$.item.id", is(bookingDtoApproved.getItem().getId().intValue())))
                .andExpect(jsonPath("$.item.name", is(bookingDtoApproved.getItem().getName())))
                .andExpect(jsonPath("$.item.description", is(bookingDtoApproved.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", is(bookingDtoApproved.getItem().getAvailable())))
                .andExpect(jsonPath("$.booker.id", is(bookingDtoApproved.getBooker().getId().intValue())))
                .andExpect(jsonPath("$.booker.name", is(bookingDtoApproved.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", is(bookingDtoApproved.getBooker().getEmail())))
                .andExpect(jsonPath("$.status", is(bookingDtoApproved.getStatus().toString())));
    }

    @Test
    void getById() throws Exception {
        when(bookingService.get(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId().intValue())))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.item.description", is(bookingDto.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", is(bookingDto.getItem().getAvailable())))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId().intValue())))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", is(bookingDto.getBooker().getEmail())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void getAllBookingUser() throws Exception {
        when(bookingService.getAllBookingUser(anyLong(), any(State.class)))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings?state={state}", State.ALL)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId().intValue())))
                .andExpect(jsonPath("$[0].item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", is(bookingDto.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", is(bookingDto.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId().intValue())))
                .andExpect(jsonPath("$[0].booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$[0].booker.email", is(bookingDto.getBooker().getEmail())))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void getAllBookingOwner() throws Exception {
        when(bookingService.getAllBookingOwner(anyLong(), any(State.class)))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner?state={state}", State.ALL)
                        .header("X-Sharer-User-Id", 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId().intValue())))
                .andExpect(jsonPath("$[0].item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", is(bookingDto.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", is(bookingDto.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId().intValue())))
                .andExpect(jsonPath("$[0].booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$[0].booker.email", is(bookingDto.getBooker().getEmail())))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().toString())));
    }
}