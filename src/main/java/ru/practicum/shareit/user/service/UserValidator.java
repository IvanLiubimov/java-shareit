package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.user.model.User;

@Component
@RequiredArgsConstructor
public class UserValidator {

    public void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new ConditionsNotMetException("Укажите имя пользователя");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ConditionsNotMetException("Имейл должен быть указан и содержать символ @");
        }
    }

    public void validateForPatch(User user) {
        boolean hasName = user.getName() != null;
        boolean hasEmail = user.getEmail() != null;

        if (!hasName && !hasEmail) {
            throw new ConditionsNotMetException("Должно быть заполнено хотя бы одно поле: name или email");
        }

        if (hasName) {
            if (user.getName().isBlank()) {
                throw new ConditionsNotMetException("Имя не может быть пустым");
            }
        }

        if (hasEmail) {
            if (user.getEmail().isBlank()) {
                throw new ConditionsNotMetException("Email не может быть пустым");
            }
            if (!user.getEmail().contains("@")) {
                throw new ConditionsNotMetException("Email должен содержать символ '@'");
            }
        }
    }
}


