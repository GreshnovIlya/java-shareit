package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ItemAnswerDto {
    private Long id;
    private String name;
    private Long ownerId;
}
