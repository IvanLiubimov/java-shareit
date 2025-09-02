package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Collection<Item> getListOfItems(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Item createItem(Item item) {
        Long itemId = generateId();
        item.setId(itemId);
        items.put(itemId, item);
        return item;
    }

    @Override
    public Item updateItem(Item item, Item newItem) {
        if (newItem.getName() != null && !newItem.getName().isBlank()) {
            item.setName(newItem.getName());
        }
        if (newItem.getDescription() != null && !newItem.getDescription().isBlank()) {
            item.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) {
            item.setAvailable(newItem.getAvailable());
        }
        return item;
    }

    @Override
    public Optional<Item> getItemById(Long id) {
        Item item = items.get(id);
        return Optional.of(item);
    }

    @Override
    public Collection<Item> searchItem(String query) {
        String lowerQuery = query.toLowerCase();

        return items.values().stream()
                .filter(Item::getAvailable)
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

