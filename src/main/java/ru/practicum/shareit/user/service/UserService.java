package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {

    Collection<UserDto> getListOfUsers();

    UserDto getUser(Long userId);

    UserDto createUser(UserDto userDto);

    UserDto editUser(UserDto newUser, Long id);

    void deleteUser(Long id);
}
