package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoAll;


import java.util.Collection;

@Service
public interface ItemService {

    Collection<ItemDtoAll> getAllItems(Long userId);

    ItemDtoAll getItemById(Long id, Long userId);

    ItemDto createItem(ItemDto itemDto, Long userId);

    ItemDto editItem(Long itemId, ItemDto itemDto, Long userId);

    Collection<ItemDto> searchItems(String query, Long userId);

    CommentDto addComment(Long itemId, Long userId, CommentDto comment);
}
