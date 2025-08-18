package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collection;

@Data
@AllArgsConstructor
public class ItemDtoAll {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private Long owner;
    private LocalDate lastBooking;
    private LocalDate nextBooking;
    private Collection<CommentDto> comments;
}
