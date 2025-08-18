package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.item.model.Comment;


@Component
@RequiredArgsConstructor
public class CommentValidator {

    public void validate(Comment comment) {
        if (comment.getText() == null || comment.getText().isBlank()) {
            throw new ConditionsNotMetException("Описание комментария не может быть пустым");
        }
        if (comment.getCreated() == null) {
            throw new ConditionsNotMetException("Creation time cannot be null");
        }
    }
}
