package ru.practicum.shareit.booking;

/**
 * TODO Sprint add-bookings.
 */
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

@Data
@RequiredArgsConstructor
public class Booking {
    private long id;
    private Instant start;
    private Instant end;
    private Item item;
    private User booker;
    private Status status;
}