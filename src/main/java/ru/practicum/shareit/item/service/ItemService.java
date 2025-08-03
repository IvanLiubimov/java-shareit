package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;


import java.util.Collection;

@Service
public interface ItemService {

    Collection<Item> getAllItems(Long userId);

    Item getItemById(Long id);

    Item createItem(ItemDto itemDto, Long userId);

    Item editItem(Long itemId, ItemDto itemDto, Long userId);

    Collection<ItemDto> searchItems(String query, Long userId);
}
