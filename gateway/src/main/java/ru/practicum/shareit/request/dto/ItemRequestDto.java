package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.item.dto.ItemAnswerDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ItemRequestDto {
    private Long id;

    @NotBlank(message = "Описание не должно быть пустым")
    private String description;

    private LocalDateTime created;

    private UserDto requester;

    private List<ItemAnswerDto> items;
}
