package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.item.dto.ItemDto;

@Component
@RequiredArgsConstructor
public class ItemValidator {

    public void validate(ItemDto itemDto, Long userId) {

        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new ConditionsNotMetException("Имя вещи не может быть пустым");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание вещи не может быть пустым");
        }

        if (itemDto.getAvailable() == null) {
            throw new ConditionsNotMetException("Поле доступности вещи обязательно");
        }
    }

    public void validateForUpdate(Long itemId, ItemDto itemDto, Long userId) {

        boolean isNamePresent = itemDto.getName() != null && !itemDto.getName().isBlank();
        boolean isDescriptionPresent = itemDto.getDescription() != null && !itemDto.getDescription().isBlank();
        boolean isAvailablePresent = itemDto.getAvailable() != null;

        if (!isNamePresent && !isDescriptionPresent && !isAvailablePresent) {
            throw new ConditionsNotMetException("Нужно указать хотя бы одно поле для обновления");
        }

        if (itemDto.getName() != null && itemDto.getName().isBlank()) {
            throw new ConditionsNotMetException("Имя не может быть пустым");
        }
        if (itemDto.getDescription() != null && itemDto.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
    }
}

