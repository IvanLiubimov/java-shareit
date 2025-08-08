package ru.practicum.shareit.item.storage;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

@Component
public interface ItemStorage {

    Collection<Item> getListOfItems(Long userId);

    Item createItem(Item item);

    Item updateItem(Item item, ItemDto itemDto);

    Optional<Item> getItemById(Long id);

    Collection<Item> searchItem(String query, Long userId);


}
