package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.InMemoryItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final InMemoryUserStorage inMemoryUserStorage;
    private final InMemoryItemStorage inMemoryItemStorage;
    private final ItemValidator itemValidator;
    private final ItemMapper itemMapper;

    public Collection<Item> getAllItems(Long userId) {
        return inMemoryItemStorage.getListOfItems(userId);
    }

    public Item getItemById(Long id) {
        return inMemoryItemStorage.getItemById(id);
    }

    public Item createItem(ItemDto itemDto, Long userId) {
        User owner = inMemoryUserStorage.getUserById(userId);
        itemValidator.validate(itemDto, userId);
        return inMemoryItemStorage.createItem(itemDto, owner);
    }

    public Item editItem(Long itemId, ItemDto itemDto, Long userId) {
        itemValidator.validateForUpdate(itemId, itemDto, userId);
        return inMemoryItemStorage.updateItem(itemId, itemDto, userId);
    }

    public Collection<ItemDto> searchItems(String query, Long userId) {
        return inMemoryItemStorage.searchItem(query, userId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

}
