package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class BookingValidator {

    public void validate(BookingRequestDto booking) {
        if (booking.getStart() == null || booking.getEnd() == null) {
            throw new ConditionsNotMetException("Начальное и конечное время аренды не должны быть пустыми");
        }

        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new ConditionsNotMetException("Время завершения аренды должно быть позже времени начала аренды");
        }

        if (booking.getStart().isBefore(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()))) {
            throw new ConditionsNotMetException("Время начала не может быть в прошлом");
        }
        if (booking.getItemId() == null) {
            throw new ConditionsNotMetException("Id вещи не может быть null");
        }
    }


}
