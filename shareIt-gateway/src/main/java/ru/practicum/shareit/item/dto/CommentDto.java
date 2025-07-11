package ru.practicum.shareit.item.dto;

import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;

    @NotBlank(message = "Комментарий не должен быть пустым", groups = Default.class)
    private String text;

    private String authorName;

    private LocalDateTime created;
}
