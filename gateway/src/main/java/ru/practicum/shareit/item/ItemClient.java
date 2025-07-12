package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient  {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(NewItemDto item, long owner) {
        return post("", owner, item);
    }

    public ResponseEntity<Object> updateItem(ItemDto item, Long owner, Long itemId) {
        return patch("" + itemId, owner, item);
    }

    public ResponseEntity<Object> getItemById(long id) {
        return get("/" + id);
    }

    public ResponseEntity<Object> getAllItemByOwner(long owner) {
        return get("", owner);
    }

    public ResponseEntity<Object> getItemsByNameOrDescription(String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search?text={text}", 0L, parameters);
    }

    public ResponseEntity<Object> createComment(CommentDto commentDto, long authorId, long itemId) {
        Map<String, Object> parameters = Map.of(
                "itemId", itemId
        );
        return post("/{itemId}/comment", authorId, parameters, commentDto);
    }
}
