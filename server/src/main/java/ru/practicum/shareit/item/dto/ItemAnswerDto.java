package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class ItemAnswerDto {
    private Long id;
    private String name;
    private Long ownerId;
}