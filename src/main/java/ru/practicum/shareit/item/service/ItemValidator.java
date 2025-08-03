package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

@Component
@RequiredArgsConstructor
public class ItemValidator {
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    public void validate(ItemDto itemDto, Long userId) {

        if (itemDto.getName() == null || itemDto.getName().trim().isEmpty()) {
            throw new ConditionsNotMetException("Имя вещи не может быть пустым");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isEmpty()) {
            throw new ConditionsNotMetException("Описание вещи не может быть пустым");
        }
        if (userStorage.getUserById(userId) == null) {
            throw new ConditionsNotMetException("Вещь может создавать только существующий пользователь");
        }
        if (itemDto.getAvailable() == null) {
            throw new ConditionsNotMetException("Поле доступности вещи обязательно");
        }
        try {
            userStorage.getUserById(userId);
        } catch (NotFoundException e) {
            throw new ConditionsNotMetException("Вещь может создавать только существующий пользователь");
        }
    }

    public void validateForUpdate(Long itemId, ItemDto itemDto, Long userId) {
        Item existingItem = itemStorage.getItemById(itemId);
        if (existingItem == null) {
            throw new NotFoundException("Вещь с id = " + itemId + " не найдена");
        }

        if (!existingItem.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не владелец вещи с id = " + itemId);
        }

        boolean isNamePresent = itemDto.getName() != null;
        boolean isDescriptionPresent = itemDto.getDescription() != null;
        boolean isAvailablePresent = itemDto.getAvailable() != null;

        if (!isNamePresent && !isDescriptionPresent && !isAvailablePresent) {
            throw new ConditionsNotMetException("Нужно указать хотя бы одно поле для обновления");
        }

        if (itemDto.getAvailable() != null && !(itemDto.getAvailable() instanceof Boolean)) {
            throw new ConditionsNotMetException("Поле доступности должно быть true или false");
        }
    }
}

