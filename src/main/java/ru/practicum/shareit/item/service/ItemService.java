package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;


import java.util.Collection;
import java.util.Optional;

@Service
public interface ItemService {

    Collection<ItemDto> getAllItems(Long userId);

    Optional<ItemDto> getItemById(Long id);

    ItemDto createItem(ItemDto itemDto, Long userId);

    ItemDto editItem(Long itemId, ItemDto itemDto, Long userId);

    Collection<ItemDto> searchItems(String query, Long userId);
}
