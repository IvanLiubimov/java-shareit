package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.ErrorHandler;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserStorage userStorage;

    public boolean validate(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            return false;
        }
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ConditionsNotMetException("Имейл должен быть указан и содержать символ @");
        }
        return true;
    }

    public boolean validateForPatch(User user, Long existingUserId) {
        if (user.getName() != null && user.getName().isEmpty()) {
            throw new ConditionsNotMetException("Имя не может быть пустым");
        }
        if (user.getEmail() != null) {
            if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
                throw new ConditionsNotMetException("Имейл должен быть непустым и содержать символ '@'");
            }

            boolean emailExists = userStorage.getListOfUsers().stream()
                    .anyMatch(u -> u.getEmail() != null &&
                            u.getEmail().equalsIgnoreCase(user.getEmail()) &&
                            !u.getId().equals(user.getId()));

            if (emailExists) {
                throw new ErrorHandler.ConflictException("Email уже используется другим пользователем");
            }
        }
        return true;
    }

}

