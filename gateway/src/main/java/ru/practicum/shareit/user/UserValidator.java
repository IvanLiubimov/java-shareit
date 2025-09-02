package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.user.dto.UserDto;

@Component
@RequiredArgsConstructor
public class UserValidator {

    public void validate(UserDto user) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new ConditionsNotMetException("Укажите имя пользователя");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ConditionsNotMetException("Имейл должен быть указан и содержать символ @");
        }
    }

    public void validateForPatch(UserDto user) {
        boolean hasName = user.getName() != null &&  !user.getName().isBlank();
        boolean hasEmail = user.getEmail() != null &&  !user.getEmail().isBlank();

        if (!hasName && !hasEmail) {
            throw new ConditionsNotMetException("Должно быть заполнено хотя бы одно поле: name или email");
        }
    }
}


