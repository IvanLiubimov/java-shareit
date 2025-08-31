package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;
    private final ItemRequestDtoValidator itemRequestDtoValidator;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader ("X-Sharer-User-Id") Long userId,
                                                      @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Получен запрос на создание запроса вещи");
        itemRequestDtoValidator.validate(itemRequestDto);
        return itemRequestClient.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemRequestsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на получение всех вещей пользователя");
        return itemRequestClient.getAllItemRequestsOfUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests() {
        log.info("Получен запрос на получение всех запросов вещей пользователя");
        return itemRequestClient.getAllItemRequests();
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@PathVariable("requestId") Long requestId) {
        log.info("Получен запрос на получение запроса вещи по Id");
        return itemRequestClient.getItemRequestById(requestId);
    }
}
