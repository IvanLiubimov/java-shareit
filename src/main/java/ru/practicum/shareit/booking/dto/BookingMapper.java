package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    public BookingDto toBookingDto(Booking booking, ItemDto itemDto) {

        UserDto userDto = booking.getBooker() != null ?
                UserMapper.toUserDto(booking.getBooker()) : null;

        return new BookingDto(
                booking.getId(),
                toLocalDateTime(booking.getStart()),
                toLocalDateTime(booking.getEnd()),
                itemDto,
                userDto,
                booking.getStatus()
        );
    }

    public Booking toBooking(BookingRequestDto bookingDto, Item item, User booker, Status status) {
        Booking booking = new Booking();
        booking.setStart(toInstant(bookingDto.getStart()));
        booking.setEnd(toInstant(bookingDto.getEnd()));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(status);
        return booking;
    }

    private LocalDateTime toLocalDateTime(Instant instant) {
        return instant != null
                ? LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                : null;
    }

    private Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime != null
                ? localDateTime.atZone(ZoneId.systemDefault()).toInstant()
                : null;
    }
}