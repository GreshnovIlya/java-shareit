package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comment toComment(CommentDto commentDto) {
        return new Comment(
                0L,
                commentDto.getText(),
                new Item(),
                new User(),
                LocalDateTime.now()
        );
    }

    public static CommentDto toCommentDto(Comment commentDto) {
        return new CommentDto(
                commentDto.getId(),
                commentDto.getText(),
                commentDto.getAuthor().getName(),
                commentDto.getCreated()
        );
    }
}
