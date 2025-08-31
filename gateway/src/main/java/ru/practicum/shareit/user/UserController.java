package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;
    private final UserValidator userValidator;

    @GetMapping
    public ResponseEntity<Object> getListOfUsers() {
        log.info("Получен HTTP запрос на получение всех пользователей");
        return userClient.getListOfUsers();

    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        log.info("Получен HTTP запрос на получение пользователя по id: {}", userId);
        return userClient.getUser(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserDto userDto) {
        log.info("Получен HTTP запрос на создание пользователя: {}", userDto);
        userValidator.validate(userDto);
        return userClient.createUser(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> editUser(
            @PathVariable Long id,
            @RequestBody UserDto userDto
    ) {
        log.info("HTTP PATCH /users/{} body={}", id, userDto);
        userValidator.validateForPatch(userDto);
        return userClient.editUser(userDto, id);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(
            @PathVariable Long id
    ) {
        log.info("Принят HTTP запрос на удаление пользователя с id: {}", id);
        userClient.deleteUser(id);
        return ResponseEntity.noContent().build();
    }







}

