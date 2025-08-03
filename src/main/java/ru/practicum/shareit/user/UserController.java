package ru.practicum.shareit.user;

import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> getListOfUsers() {
        log.info("Получен HTTP запрос на получение всех пользователей");
        return userService.getListOfUsers();

    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        log.info("Получен HTTP запрос на получение пользователя по id: {}", userId);
        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Получен HTTP запрос на создание пользователя: {}", user);
        return userService.createUser(user);
    }

    @PatchMapping ("/{id}")
    public User editUser(
            @PathVariable Long id,
            @RequestBody User newUser
    ) {
        log.info("Принят HTTP запрос на обновление пользователя: {}", newUser);
        return userService.editUser(newUser, id);
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

