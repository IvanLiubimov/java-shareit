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
    public UserDto getUser(Long userId) {
        User user = getUserIfExists(userId);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        if (userStorage.isEmailExists(user)) {
            throw new ErrorHandler.ConflictException("Email уже используется другим пользователем");
        }
        userValidator.validate(user);
        User createdUser = userStorage.createUser(user);
        return UserMapper.toUserDto(createdUser);
    }

    @Override
    public UserDto editUser(UserDto newUserDto, Long id) {
        User newUser = UserMapper.toUser(newUserDto);
        getUserIfExists(id);
        if (newUser.getEmail() != null && userStorage.isEmailExists(newUser)) {
            throw new ErrorHandler.ConflictException("Email уже используется другим пользователем");
        }
        userValidator.validateForPatch(newUser);
        User updatedUser = userStorage.updateUser(newUser, id);
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        getUserIfExists(id);
        userStorage.deleteUser(id);
    }

    public User getUserIfExists(Long userId) {
        return userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
    }
}