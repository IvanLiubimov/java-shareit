package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> getListOfUsers() {
        return users.values();
    }

    @Override
    public User createUser(User user) {
        long userId = generateId();
        user.setId(userId);
        users.put(userId, user);
        log.info("Пользователь создан: {}", user);
        return user;
    }

    @Override
    public User updateUser(User newUser, Long id) {

        log.trace("Пользователь найден");
        User oldUser = users.get(id);

        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) {
            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getName() != null && !newUser.getName().isBlank()) {
            oldUser.setName(newUser.getName());
        }

        log.info("Успешно обновил пользователя: {}", oldUser);
        return oldUser;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    private long generateId() {
        long maxId = users.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        maxId++;
        return maxId;
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    public boolean isEmailExists(User user) {
        if (user.getEmail() == null) {
            return false;
        }

        return getListOfUsers().stream()
                .filter(u -> !u.getId().equals(user.getId()))
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(user.getEmail()));
    }
}