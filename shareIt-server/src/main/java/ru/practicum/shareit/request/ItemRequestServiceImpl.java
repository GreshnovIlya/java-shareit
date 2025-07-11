package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemRequestDto create(ItemRequestDto itemRequestDto, Long requesterId) {
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        if (itemRequest.getDescription().isBlank()) {
            throw new BadRequestException("Описание не должно быть пустым");
        }
        itemRequest.setRequester(userRepository.findById(requesterId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id = %s не найден", requesterId))));
        itemRequest.setCreated(LocalDateTime.now());
        ItemRequest newItemRequest = itemRequestRepository.save(itemRequest);
        log.info("Создан запрос на вещь: {}", newItemRequest);
        return ItemRequestMapper.toItemRequestDto(newItemRequest, List.of());
    }

    @Override
    public List<ItemRequestDto> getByRequesterId(Long requesterId) {
        return itemRequestRepository.findByRequesterIdOrderByCreatedDesc(requesterId)
                .stream().map(itemRequest -> ItemRequestMapper.toItemRequestDto(itemRequest,
                            itemRepository.findByRequestId(itemRequest.getId())
                                    .stream().map(ItemMapper::toItemAnswerDto).toList())
                ).toList();
    }

    @Override
    public List<ItemRequestDto> getOthers(Long userId) {
        return itemRequestRepository.findByRequesterIdNotOrderByCreatedDesc(userId)
                .stream().map(itemRequest -> ItemRequestMapper.toItemRequestDto(itemRequest,
                        itemRepository.findByRequestId(itemRequest.getId())
                                .stream().map(ItemMapper::toItemAnswerDto).toList()))
                .toList();
    }

    public ItemRequestDto getById(Long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос с id = %s не найден", requestId)));
        return ItemRequestMapper.toItemRequestDto(itemRequest,
                itemRepository.findByRequestId(itemRequest.getId())
                        .stream().map(ItemMapper::toItemAnswerDto).toList());
    }
}
