package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ErrorHandler;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> users = new HashMap<>();

    public Collection<User> getListOfUsers() {
        return users.values();
    }

    @Override
    public User createUser(User user) {
        if (isEmailExists(user.getEmail())) {
            throw new ErrorHandler.ConflictException("Такой имейл уже есть у одного из пользователей");
        }
        long userId = generateId();
        user.setId(userId);
        users.put(userId, user);
        log.info("Пользователь создан: {}", user);
        return user;
    }

    @Override
    public User updateUser(User newUser, Long id) {
        if (id == null) {
            long newUserId = users.values().stream()
                    .filter(user1 -> user1.getEmail().equals(newUser.getEmail()))
                    .map(User::getId)
                    .findFirst()
                    .orElse(0L);
            newUser.setId(newUserId);
        }
        if (users.containsKey(id)) {
            log.trace("Пользователь найден");
            User oldUser = users.get(id);

            oldUser.setEmail(newUser.getEmail());
            oldUser.setName(newUser.getName());

            log.info("Успешно обработал HTTP запрос на обновление пользователя: {}", newUser);
            return oldUser;

        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }


    @Override
    public User getUserById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
        return user;
    }

    private boolean isEmailExists(User user) {
        if (users.values().stream()
                .anyMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
            throw new ErrorHandler.ConflictException("Такой имеил уже есть у одного из пользователей");
        }
        return false;
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
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
        users.remove(id);
    }

    @Override
    public boolean isEmailExists(String email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }
}