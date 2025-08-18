package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.Collection;

@Service
public interface BookingService {

    BookingDto createBooking(BookingRequestDto bookingRequestDto, Long userId);

    BookingDto approveBooking(Long bookingId, Boolean approved, Long userId);

    BookingDto getBookingById(Long bookingId, Long userId);

    Collection<BookingDto> getAllBookings(String state, Long userId);

    Collection<BookingDto> findBookingsForOwnerItems(String bookingState, Long userId);
}
