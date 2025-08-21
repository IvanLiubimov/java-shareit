package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.ErrorHandler;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    @Override
    public Collection<UserDto> getListOfUsers() {
        Collection<User> users = userRepository.findAll();
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
        userValidator.validate(user);
        try {
            userRepository.save(user);
            return UserMapper.toUserDto(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Пользователь с email " + userDto.getEmail() + " уже существует");
        }
    }

    @Override
    public UserDto editUser(UserDto newUserDto, Long id) {
        User newUser = UserMapper.toUser(newUserDto);
        User oldUser = getUserIfExists(id);
        try {
            userValidator.validateForPatch(newUser);
            UserMapper.updateUserFromDto(newUserDto, oldUser);
            User updatedUser = userRepository.save(oldUser);
            return UserMapper.toUserDto(updatedUser);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Пользователь с email " + newUserDto.getEmail() + " уже существует");
        }
    }

    @Override
    public void deleteUser(Long id) {
        getUserIfExists(id);
        userRepository.deleteById(id);
    }

    private User getUserIfExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
    }
}