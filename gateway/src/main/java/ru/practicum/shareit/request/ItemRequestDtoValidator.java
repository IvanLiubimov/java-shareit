package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.request.dto.ItemRequestDto;


@Component
public class ItemRequestDtoValidator {

    public void validate (ItemRequestDto itemRequestDto) {
        if (itemRequestDto.getDescription() == null || itemRequestDto.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание запроса не может быть пустым");
        }

    }
}
