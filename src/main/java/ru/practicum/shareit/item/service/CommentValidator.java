package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.item.dto.CommentDto;


@Component
@RequiredArgsConstructor
public class CommentValidator {

    public void validate(CommentDto comment) {
        if (comment.getText() == null || comment.getText().isBlank()) {
            throw new ConditionsNotMetException("Описание комментария не может быть пустым");
        }
    }
}
