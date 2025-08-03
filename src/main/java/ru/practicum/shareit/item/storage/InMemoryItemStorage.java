package ru.practicum.shareit.item.storage;


import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryItemStorage implements ItemStorage {

    private Map<Long, Item> items = new HashMap<>();

    @Override
    public Collection<Item> getListOfItems(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Item createItem(ItemDto itemDto, User owner) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(Boolean.TRUE.equals(itemDto.getAvailable()));
        item.setOwner(owner);
        item.setId(generateId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Long itemId, ItemDto itemDto, Long userId) {
        Item item = items.get(itemId);
        if (item == null || !item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Вещь с id = " + itemDto.getId() + " не найдена или не принадлежит пользователю");
        }
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(Boolean.TRUE.equals(itemDto.getAvailable()));
        return item;
    }

    @Override
    public Item getItemById(Long id) {
        Item item = items.get(id);
        return item;
    }

    @Override
    public Collection<Item> searchItem(String query, Long userId) {
        if (query == null || query.isBlank()) {
            throw new ConditionsNotMetException("Ваш запрос пуст");
        }

        String lowerQuery = query.toLowerCase();

        return items.values().stream()
                .filter(Item::isAvailable)
                .filter(item -> {
                    String name = item.getName();
                    String desc = item.getDescription();
                    return (name != null && name.toLowerCase().contains(lowerQuery)) ||
                            (desc != null && desc.toLowerCase().contains(lowerQuery));
                })
                .collect(Collectors.toList());
    }

    private long generateId() {
        long maxId = items.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        maxId++;
        return maxId;
    }

}

