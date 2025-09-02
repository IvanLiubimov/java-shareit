package ru.practicum.shareit.item.dto;

import io.micrometer.common.lang.Nullable;
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
    @Nullable
    private Long requestId;
    @Nullable
    private Long owner;
    @Nullable
    private LocalDate lastBooking;
    @Nullable
    private LocalDate nextBooking;
    private Collection<CommentDto> comments;
}
