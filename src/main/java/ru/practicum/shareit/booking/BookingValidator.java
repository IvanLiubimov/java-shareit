package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class BookingValidator {

    public void validate(Booking booking) {
        if (booking.getStart() == null || booking.getEnd() == null) {
            throw new ConditionsNotMetException("Начальное и конечное время аренды не должны быть пустыми");
        }

        if (!booking.getEnd().isAfter(booking.getStart())) {
            throw new ConditionsNotMetException("Время завершения аренды должно быть позже времени начала аренды");
        }

        if (booking.getStart().isBefore(Instant.now())) {
            throw new ConditionsNotMetException("Время начала не может быть в прошлом");
        }
        if (booking.getItem() == null) {
            throw new ConditionsNotMetException("Вещь не может быть null");
        }

        if (!booking.getItem().getAvailable()) {
            throw new ConditionsNotMetException("Вещь недоступна для бронирования");
        }
        if (booking.getBooker() == null) {
            throw new ConditionsNotMetException("Booker не может быть null");
        }

        if (booking.getItem().getOwner().equals(booking.getBooker())) {
            throw new ConditionsNotMetException("Владелиц вещи не может бронировать свою вещь");
        }
        if (booking.getStatus() == null) {
            throw new ConditionsNotMetException("Статут должен быть заполнен");
        }

        if (booking.getStatus() != Status.WAITING) {
            throw new ConditionsNotMetException("Новое бронирование должно иметь статус WAITING");
        }

    }


}
