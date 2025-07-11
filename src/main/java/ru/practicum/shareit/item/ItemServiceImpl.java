package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemServiceImpl(UserRepository userRepository, ItemRepository itemRepository, BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Item create(Item item, Long owner) {
        if (String.valueOf(item.getAvailable()).equals("null")) {
            throw new BadRequestException("Доступ не может быть null");
        }
        item.setOwner(userRepository.findById(owner).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id = %s не найден", owner))));
        Item newItem = itemRepository.save(item);
        log.info("Создан пользователь: {}", newItem);
        return newItem;
    }

    @Override
    public Item update(Item item, Long owner, Long itemId) {
        item.setOwner(userRepository.findById(owner).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id = %s не найден", owner))));
        Item updateItem = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException(String.format("Вещь с id = %s не найден", itemId)));
        if (updateItem.getOwner() != item.getOwner()) {
            throw new BadRequestException(String.format("Пользователь с id = %s не владеет данной вещью",
                    item.getOwner().getId()));
        }
        if (item.getName() != null && !item.getName().isBlank()) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            updateItem.setDescription(item.getDescription());
        }
        if (!String.valueOf(item.getAvailable()).equals("null") && !String.valueOf(item.getAvailable()).isBlank()) {
            updateItem.setAvailable(item.getAvailable());
        }
        itemRepository.save(updateItem);
        log.info("Обновлен пользователь: {}", updateItem);
        return updateItem;
    }

    @Override
    public ItemCommentDto get(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Вещь с id = %s не найден", id)));
        List<Booking> bookings =
                bookingRepository.findByItemAndStatusAndStartIsAfterOrderByStartDesc(item,
                        StatusBooking.APPROVED, LocalDateTime.now());
        if (bookings.isEmpty()) {
            return ItemMapper.toItemCommentDto(item, null, null,
                    commentRepository.findByItem(item).stream().map(CommentMapper::toCommentDto).toList());
        } else {
            return ItemMapper.toItemCommentDto(item, bookings.getLast().getStart(), bookings.getFirst().getStart(),
                    commentRepository.findByItem(item).stream().map(CommentMapper::toCommentDto).toList());
        }
    }

    @Override
    public List<ItemCommentDto> getAllItemByOwner(Long ownerId) {
        List<Item> items = itemRepository.findByOwner(userRepository.findById(ownerId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id = %s не найден", ownerId))));
        return items.stream().map(item -> {
                ItemCommentDto itemCommentDto = ItemCommentDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .request(item.getRequest())
                    .comments(commentRepository.findByItem(item).stream().map(CommentMapper::toCommentDto).toList())
                    .build();
                List<Booking> bookings =
                        bookingRepository.findByItemAndStatusAndStartIsAfterOrderByStartDesc(item,
                                StatusBooking.APPROVED, LocalDateTime.now());
                if (bookings.isEmpty()) {
                    return itemCommentDto;
                } else {
                    itemCommentDto.setNextBooking(bookings.getLast().getStart());
                    itemCommentDto.setNextBooking(bookings.getFirst().getStart());
                    return itemCommentDto;
                }

        }).toList();
    }

    @Override
    public List<Item> getItemsByNameOrDescription(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.findItemsByNameOrDescriptionContainingIgnoreCase(text);
    }

    @Override
    public Comment createComment(Comment comment, Long authorId, Long itemId) {
        User author = userRepository.findById(authorId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id = %s не найден", authorId)));
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException(String.format("Вещь с id = %s не найден", itemId)));
        if (bookingRepository.findByItemAndStatusAndBookerAndEndIsBeforeOrderByStartDesc(item,
                StatusBooking.APPROVED, author, LocalDateTime.now()).isEmpty()) {
            throw new BadRequestException(String.format("Пользователь с id = %s не брал в аренду эту вещь", authorId));
        }
        comment.setItem(item);
        comment.setAuthor(author);
        Comment newComment = commentRepository.save(comment);
        log.info("Создан отзыв: {}", newComment);
        return newComment;
    }
}
