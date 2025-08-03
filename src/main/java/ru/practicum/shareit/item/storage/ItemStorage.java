package ru.practicum.shareit.item.storage;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

@Component
public interface ItemStorage {

    Collection<Item> getListOfItems(Long userId);

    Item createItem(ItemDto itemDto, User owner);

    Item updateItem(Long itemId, ItemDto itemDto, Long userId);

    Item getItemById(Long id);

    Collection<Item> searchItem(String query, Long userId);


}
