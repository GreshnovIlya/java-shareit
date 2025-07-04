package ru.practicum.shareit.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.UpdateValidationGroup;

import java.time.Instant;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "item_requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id cannot be empty", groups = UpdateValidationGroup.class)
    private long id;

    @NotBlank(message = "Description cannot be empty", groups = {Default.class, UpdateValidationGroup.class})
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    @NotNull(message = "Requester cannot be empty", groups = {Default.class, UpdateValidationGroup.class})
    private User requester;

    private Instant created;
}
