package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;


import java.util.Collection;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @GetMapping
    public Collection<Item> getAllItems(
            @RequestHeader ("X-Sharer-User-Id") Long userId
    ) {
        log.info("Получен HTTP запрос вывод списка вещей");
        return itemService.getAllItems(userId);
    }

    @GetMapping("{id}")
    public ResponseEntity<Item> getItemById(
            @PathVariable Long id
    ) {
        log.info("Получен HTTP запрос на получение вещи по id: {}", id);
        Item item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ItemDto createItem(
            @RequestBody ItemDto itemDto,
            @RequestHeader ("X-Sharer-User-Id") Long userId
    ) {
        log.info("Получен HTTP запрос на создание вещи: {}", itemDto);
        Item item = itemService.createItem(itemDto, userId);
        return itemMapper.toItemDto(item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto editItem(
            @PathVariable Long itemId,
            @RequestBody ItemDto itemDto,
            @RequestHeader ("X-Sharer-User-Id") Long userId
    ) {
        log.info("Получен HTTP запрос на обновление вещи: {}", itemDto);
        Item item = itemService.editItem(itemId, itemDto, userId);
        return itemMapper.toItemDto(item);
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

