package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.dto.ItemRequestInItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ItemCommentDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    @Setter
    private ItemRequestInItemDto request;
    @Setter
    private LocalDateTime nextBooking;
    @Setter
    private LocalDateTime lastBooking;
    private List<CommentDto> comments;
}