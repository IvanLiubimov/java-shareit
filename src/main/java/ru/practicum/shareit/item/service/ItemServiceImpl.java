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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserStorage inMemoryUserStorage;
    private final ItemStorage inMemoryItemStorage;
    private final ItemValidator itemValidator;
    private final ItemMapper itemMapper;

    public Collection<ItemDto> getAllItems(Long userId) {
        inMemoryUserStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));

        return inMemoryItemStorage.getListOfItems(userId).stream()
                .map(itemMapper::toItemDto)
                .toList();
    }

    @Override
    public Optional<ItemDto> getItemById(Long id) {
        return inMemoryItemStorage.getItemById(id)
                .map(itemMapper::toItemDto);
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        User owner = inMemoryUserStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));

        itemValidator.validate(itemDto, userId);

        Item item = itemMapper.toItem(itemDto, owner);
        Item createdItem = inMemoryItemStorage.createItem(item);
        return itemMapper.toItemDto(createdItem);
    }

    public ItemDto editItem(Long itemId, ItemDto itemDto, Long userId) {
        itemValidator.validateForUpdate(itemId, itemDto, userId);

        inMemoryUserStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));

        Item item = inMemoryItemStorage.getItemById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + " не найдена"));
        if (item == null || !item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Вещь с id = " + itemDto.getId() + " не найдена или не принадлежит пользователю");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        Item updatedItem = inMemoryItemStorage.updateItem(item, itemDto);

        return itemMapper.toItemDto(updatedItem);
    }

    public Collection<ItemDto> searchItems(String query, Long userId) {
        return inMemoryItemStorage.searchItem(query, userId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

}
