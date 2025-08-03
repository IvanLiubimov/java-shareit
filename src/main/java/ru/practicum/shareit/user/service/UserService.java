package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final UserValidator userValidator;


    public Collection<User> getListOfUsers() {
        return userStorage.getListOfUsers();
    }

    public User getUser(Long userId) {
        return userStorage.getUserById(userId);
    }

    public User createUser(User user) {
        userValidator.validate(user);
        return userStorage.createUser(user);
    }

    public User editUser(User newUser, Long id) {
        userValidator.validateForPatch(newUser, id);
        return userStorage.updateUser(newUser, id);
    }

    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }

}