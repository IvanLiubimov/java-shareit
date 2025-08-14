package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

/**
 * TODO Sprint add-bookings.
 */
public class BookingDto {
    private long id;
    private Instant start;
    private Instant end;
    private Item item;
    private User booker;
    private Status status;
}
