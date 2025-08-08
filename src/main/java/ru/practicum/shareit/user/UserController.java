package ru.practicum.shareit.user;

import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getListOfUsers() {
        log.info("Получен HTTP запрос на получение всех пользователей");
        return userService.getListOfUsers();

    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        log.info("Получен HTTP запрос на получение пользователя по id: {}", userId);
        return userService.getUser(userId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найдена"));
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        log.info("Получен HTTP запрос на создание пользователя: {}", userDto);
        return userService.createUser(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto editUser(
            @PathVariable Long id,
            @RequestBody UserDto userDto
    ) {
        log.info("HTTP PATCH /users/{} body={}", id, userDto);
        return userService.editUser(userDto, id);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id
    ) {
        log.info("Принят HTTP запрос на удаление пользователя с id: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }







}

