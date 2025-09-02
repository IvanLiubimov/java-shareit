package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;

@Component
@RequiredArgsConstructor
public class BookingValidator {

    public void validate(BookingRequestDto booking) {
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new ConditionsNotMetException("Время завершения аренды должно быть позже времени начала аренды");
        }

    }


}
