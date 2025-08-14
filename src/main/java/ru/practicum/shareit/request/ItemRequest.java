package ru.practicum.shareit.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

@Data
@RequiredArgsConstructor
public class ItemRequest {
    private final long id;
    private final String description;
    private final User requestor;
    private final Instant created;
}