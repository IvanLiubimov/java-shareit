package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ErrorHandler;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final UserValidator userValidator;

    @Override
    public Collection<UserDto> getListOfUsers() {
        Collection<User> users = userStorage.getListOfUsers();
        return users.stream().map(UserMapper::toUserDto).toList();
    }

    @Override
    public Optional<UserDto> getUser(Long userId) {
        Optional<User> user =  userStorage.getUserById(userId);
        return Optional.of(UserMapper.toUserDto(user.get()));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        if (isEmailExists(user)) {
            throw new ErrorHandler.ConflictException("Email уже используется другим пользователем");
        }
        userValidator.validate(user);
        User createdUser = userStorage.createUser(user);
        return UserMapper.toUserDto(createdUser);
    }

    @Override
    public UserDto editUser(UserDto newUserDto, Long id) {
        User newUser = UserMapper.toUser(newUserDto);

        // Проверка на существование
        userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));

        // Проверка уникальности email
        if (newUser.getEmail() != null && isEmailExists(newUser)) {
            throw new ErrorHandler.ConflictException("Email уже используется другим пользователем");
        }

        userValidator.validateForPatch(newUser);

        User updatedUser = userStorage.updateUser(newUser, id);

        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
        userStorage.deleteUser(id);
    }

    public boolean isEmailExists(User user) {
        if (user.getEmail() == null) {
            return false;
        }

        return userStorage.getListOfUsers().stream()
                .filter(u -> u.getEmail() != null)
                .filter(u -> u.getEmail().equalsIgnoreCase(user.getEmail()))
                .anyMatch(u -> !u.getId().equals(user.getId()));
    }


}