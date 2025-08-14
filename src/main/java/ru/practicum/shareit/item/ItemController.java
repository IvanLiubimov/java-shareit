package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;


import java.util.Collection;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public Collection<ItemDto> getAllItems(
            @RequestHeader ("X-Sharer-User-Id") Long userId
    ) {
        log.info("Получен HTTP запрос вывод списка вещей");
        return itemService.getAllItems(userId);
    }

    @GetMapping("{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable Long id) {
        log.info("Получен HTTP запрос на получение вещи по id: {}", id);
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @PostMapping
    public ItemDto createItem(
            @RequestBody ItemDto itemDto,
            @RequestHeader ("X-Sharer-User-Id") Long userId
    ) {
        log.info("Получен HTTP запрос на создание вещи: {}", itemDto);
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto editItem(
            @PathVariable Long itemId,
            @RequestBody ItemDto itemDto,
            @RequestHeader ("X-Sharer-User-Id") Long userId
    ) {
        log.info("Получен HTTP запрос на обновление вещи: {}", itemDto);
        return itemService.editItem(itemId, itemDto, userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(
            @RequestParam("text") String text,
            @RequestHeader ("X-Sharer-User-Id") Long userId
    ) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return itemService.searchItems(text, userId);
    }



}

