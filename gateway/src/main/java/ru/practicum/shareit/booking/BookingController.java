package ru.practicum.shareit.booking;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;
	private final BookingValidator bookingValidator;

	@GetMapping
	public ResponseEntity<Object> getAllBookings(@RequestHeader("X-Sharer-User-Id") long userId,
			@RequestParam(name = "state", defaultValue = "all") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}

	@PostMapping
	public ResponseEntity<Object> createBooking(@RequestBody BookingRequestDto requestDto,
									@RequestHeader("X-Sharer-User-Id") Long userId) {
		log.info("Получен HTTP запрос на создание бронирования");
		bookingValidator.validate(requestDto);
		return bookingClient.bookItem(userId, requestDto);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> findBookingById(@PathVariable Long bookingId,
													  @RequestHeader("X-Sharer-User-Id") Long userId) {
		log.info("Получен HTTP запрос на получение бронирования по id: {}", bookingId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approveBooking(@PathVariable Long bookingId,
									 @RequestParam ("approved") boolean approved,
									 @RequestHeader("X-Sharer-User-Id") Long userId) {
		return bookingClient.approveBooking(bookingId, approved, userId);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> findAllItemsOfUser(@RequestParam (defaultValue = "ALL") String state,
													 @RequestHeader("X-Sharer-User-Id") Long userId) {
		log.info("Получен HTTP запрос на получение всех бронирований пользователя");
		return bookingClient.findBookingsForOwnerItems(state, userId);
	}
}
