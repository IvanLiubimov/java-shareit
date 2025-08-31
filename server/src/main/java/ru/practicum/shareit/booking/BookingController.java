package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;


    @PostMapping
    public BookingDto createBooking(@RequestBody BookingRequestDto bookingRequestDto,
                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен HTTP запрос на создание бронирования");
        return bookingService.createBooking(bookingRequestDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@PathVariable Long bookingId,
                                     @RequestParam ("approved") boolean approved,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.approveBooking(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> findBookingById(@PathVariable Long bookingId,
                                                      @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен HTTP запрос на получение бронирования по id: {}", bookingId);
        return ResponseEntity.ok(bookingService.getBookingById(bookingId, userId));
    }

    @GetMapping
    public Collection<BookingDto> getAllBookings(@RequestParam (defaultValue = "ALL") String state,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен HTTP запрос на получение всех бронирований");
        return bookingService.getAllBookings(state, userId);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> findAllItemsOfUser(@RequestParam (defaultValue = "ALL") String state,
                                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен HTTP запрос на получение всех бронирований пользователя");
        return bookingService.findBookingsForOwnerItems(state, userId);
    }

}
