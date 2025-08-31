package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoAll;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemClient;

    @GetMapping
    public Collection<ItemDtoAll> getAllItems(
            @RequestHeader ("X-Sharer-User-Id") Long userId
    ) {
        log.info("Получен HTTP запрос вывод списка вещей");
        return itemClient.getAllItems(userId);
    }

    @GetMapping("{id}")
    public ResponseEntity<ItemDtoAll> getItemById(@PathVariable Long id,
                                                  @RequestHeader ("X-Sharer-User-Id") Long userId) {
        log.info("Получен HTTP запрос на получение вещи по id: {}", id);
        return ResponseEntity.ok(itemClient.getItemById(id, userId));
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(
            @RequestBody ItemDto itemDto,
            @RequestHeader ("X-Sharer-User-Id") Long userId
    ) {
        log.info("Получен HTTP запрос на создание вещи: {}", itemDto);
        return ResponseEntity.ok(itemClient.createItem(itemDto, userId));
    }

    @PatchMapping("/{itemId}")
    public ItemDto editItem(
            @PathVariable Long itemId,
            @RequestBody ItemDto itemDto,
            @RequestHeader ("X-Sharer-User-Id") Long userId
    ) {
        log.info("Получен HTTP запрос на обновление вещи: {}", itemDto);
        return itemClient.editItem(itemId, itemDto, userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(
            @RequestParam("text") String text,
            @RequestHeader ("X-Sharer-User-Id") Long userId
    ) {
        return itemClient.searchItems(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable Long itemId,
                                 @RequestHeader ("X-Sharer-User-Id") Long userId,
                                 @RequestBody CommentDto comment) {
        log.info("Получен HTTP запрос на добавление комментария");
        return itemClient.addComment(itemId, userId, comment);
    }
}

