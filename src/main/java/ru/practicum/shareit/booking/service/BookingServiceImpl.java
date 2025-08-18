package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingValidator;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingValidator bookingValidator;
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;

    @Override
    public BookingDto createBooking(BookingRequestDto bookingRequestDto, Long userId) {

        Long itemId = bookingRequestDto.getItemId();
        User booker = getUserIfExists(userId);
        Item item = getItemIfExists(itemId);


        if (!item.getAvailable()) {
            throw new ConditionsNotMetException("Вещь не доступна для бронирования");
        }

        Booking booking = bookingMapper.toBooking(bookingRequestDto, item, booker, Status.WAITING);

        bookingValidator.validate(booking);

        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto approveBooking(Long bookingId, Boolean approved, Long userId) {
        Booking booking = getBookingIfExists(bookingId);
        Item item = booking.getItem();

        if (!item.getOwner().getId().equals(userId)) {
            throw new ConditionsNotMetException("Бронирование может подтверждать только владелец вещи");
        }

        if (booking.getStatus() != Status.WAITING) {
            throw new ConditionsNotMetException("Можно изменять только бронирования в статусе WAITING");
        }

        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        getUserIfExists(userId);
        Booking booking = getBookingIfExists(bookingId);
        Item item = booking.getItem();
        Long itemOwnerId = item.getOwner().getId();
        if (!booking.getBooker().getId().equals(userId) && !itemOwnerId.equals(userId)) {
            throw new ConditionsNotMetException("Получение данных о конкретном бронировании может получать " +
                    "либо владелец вещи либо автор бронирования");
        }
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public Collection<BookingDto> getAllBookings(String state, Long userId) {


        String normalizedState = state.toUpperCase();
        if (!List.of("ALL", "CURRENT", "PAST", "FUTURE", "WAITING", "REJECTED").contains(normalizedState)) {
            throw new IllegalArgumentException("Неверный статус для поиска " + state);
        }

        return bookingRepository.findAll(state, userId)
                .stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<BookingDto> findBookingsForOwnerItems(String bookingState, Long userId) {
        //String normalizedState = bookingState.toUpperCase();
        getUserIfExists(userId);
        if (!List.of("ALL", "CURRENT", "PAST", "FUTURE", "WAITING", "REJECTED").contains(bookingState)) {
            throw new IllegalArgumentException("Неверный статус для поиска " + bookingState);
        }
        return bookingRepository.getBookingsForOwnerItems(bookingState, userId)
                .stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private User getUserIfExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
    }

    private Item getItemIfExists(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + " не найдена"));
    }

    private Booking getBookingIfExists(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id = " + bookingId + " не найдено"));
    }
}
