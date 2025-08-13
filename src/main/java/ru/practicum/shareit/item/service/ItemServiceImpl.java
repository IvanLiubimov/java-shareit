package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserStorage inMemoryUserStorage;
    private final ItemStorage inMemoryItemStorage;
    private final ItemValidator itemValidator;
    private final ItemMapper itemMapper;

    public Collection<ItemDto> getAllItems(Long userId) {
        getUserIfExists(userId);
        return inMemoryItemStorage.getListOfItems(userId).stream()
                .map(itemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto getItemById(Long id) {
        Item item = getItemIfExists(id);
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        User owner = getUserIfExists(userId);

        itemValidator.validate(itemDto);

        Item item = itemMapper.toItem(itemDto, owner);
        Item createdItem = inMemoryItemStorage.createItem(item);
        return itemMapper.toItemDto(createdItem);
    }

    public ItemDto editItem(Long itemId, ItemDto itemDto, Long userId) {
        itemValidator.validateForUpdate(itemDto);
        getUserIfExists(userId);
        Item item = getItemIfExists(itemId);

        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Вещь с id = " + itemDto.getId() + " не найдена или не принадлежит пользователю");
        }

        Item newItem = itemMapper.toItem(itemDto, item.getOwner());
        Item updatedItem = inMemoryItemStorage.updateItem(item, newItem);

        return itemMapper.toItemDto(updatedItem);
    }

    public Collection<ItemDto> searchItems(String query, Long userId) {
        getUserIfExists(userId);
        return inMemoryItemStorage.searchItem(query).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private User getUserIfExists(Long userId) {
        return inMemoryUserStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
    }

    private Item getItemIfExists(Long itemId) {
        return inMemoryItemStorage.getItemById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + " не найдена"));
    }

}
