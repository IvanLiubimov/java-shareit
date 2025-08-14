package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

@Data
@RequiredArgsConstructor
public class ItemRequestDto {
    private final long id;
    private final String description;
    private final User requestor;
    private final Instant created;
}