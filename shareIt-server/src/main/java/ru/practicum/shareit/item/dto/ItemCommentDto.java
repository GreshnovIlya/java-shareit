package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestInItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCommentDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private ItemRequestInItemDto request;
    private LocalDateTime nextBooking;
    private LocalDateTime lastBooking;
    private List<CommentDto> comments;
}
