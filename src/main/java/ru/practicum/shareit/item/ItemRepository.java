package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwner(User owner);

    @Query(value = "SELECT * FROM items WHERE ((name ilike ('%' || ?1 || '%'))" +
            " OR (description ilike ('%' || ?1 || '%'))) AND available=true",
            nativeQuery = true)
    List<Item> findItemsByNameOrDescriptionContainingIgnoreCase(String text);
}
