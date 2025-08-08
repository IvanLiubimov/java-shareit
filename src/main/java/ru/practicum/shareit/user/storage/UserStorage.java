package ru.practicum.shareit.user.storage;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

@Component
public interface UserStorage {

    Collection<User> getListOfUsers();

    User createUser(User user);

    User updateUser(User user, Long id);

    Optional<User> getUserById(Long id);

    void deleteUser(Long id);

    boolean isEmailExists(String email);
}