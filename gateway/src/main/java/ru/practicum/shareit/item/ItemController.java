package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoAll;


import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;
    private final ItemValidator itemValidator;
    private final CommentValidator commentValidator;

    @GetMapping
    public ResponseEntity<Object> getAllItems(
            @RequestHeader ("X-Sharer-User-Id") Long userId
    ) {
        log.info("Получен HTTP запрос вывод списка вещей");
        return itemClient.getAllItems(userId);
    }

    @GetMapping("{id}")
    public ResponseEntity<ItemDtoAll> getItemById(@PathVariable Long id,
                                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен HTTP запрос на получение вещи по id: {}", id);
        return itemClient.getItemById(id, userId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(
            @RequestBody ItemDto itemDto,
            @RequestHeader ("X-Sharer-User-Id") Long userId
    ) {
        log.info("Получен HTTP запрос на создание вещи: {}", itemDto);
        itemValidator.validate(itemDto);
        return itemClient.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> editItem(
            @PathVariable Long itemId,
            @RequestBody ItemDto itemDto,
            @RequestHeader ("X-Sharer-User-Id") Long userId
    ) {
        log.info("Получен HTTP запрос на обновление вещи: {}", itemDto);
        itemValidator.validateForUpdate(itemDto);
        return itemClient.editItem(itemId, itemDto, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(
            @RequestParam("text") String text,
            @RequestHeader ("X-Sharer-User-Id") Long userId
    ) {
        if (text == null || text.isBlank()) {
            List<Object> objectList = Collections.emptyList();
            return ResponseEntity.ok(objectList);
        }
        return itemClient.searchItems(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable Long itemId,
                                 @RequestHeader ("X-Sharer-User-Id") Long userId,
                                 @RequestBody CommentDto comment) {
        log.info("Получен HTTP запрос на добавление комментария");
        commentValidator.validate(comment);
        return itemClient.addComment(itemId, userId, comment);
    }
}

