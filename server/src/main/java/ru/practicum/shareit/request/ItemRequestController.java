package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.service.ItemRequestService;
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

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<ItemRequestDto> createItemRequest(@RequestHeader ("X-Sharer-User-Id") Long userId,
                                                      @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Получен запрос на создание запроса вещи");
        return ResponseEntity.ok(itemRequestService.createItemRequest(userId, itemRequestDto));
    }

    @GetMapping
    public Collection<ItemRequestDto> getAllItemRequestsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на получение всех вещей пользователя");
        return itemRequestService.getAllItemRequestsOfUser(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllItemRequests() {
        log.info("Получен запрос на получение всех запросов вещей пользователя");
        return itemRequestService.getAllItemRequests();
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@PathVariable("requestId") Long requestId) {
        log.info("Получен запрос на получение запроса вещи по Id");
        return itemRequestService.getItemRequestById(requestId);
    }
}
