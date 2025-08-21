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
import ru.practicum.shareit.booking.repository.BookingState;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingValidator bookingValidator;
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;

    @Override
    public BookingDto createBooking(BookingRequestDto bookingRequestDto, Long userId) {

        bookingValidator.validate(bookingRequestDto);
        Long itemId = bookingRequestDto.getItemId();
        User booker = getUserIfExists(userId);
        Item item = getItemIfExists(itemId);
        if (item.getOwner().getId().equals(booker.getId())) {
            throw new ConditionsNotMetException("Владелиц вещи не может бронировать свою вещь");
        }

        if (!item.getAvailable()) {
            throw new ConditionsNotMetException("Вещь не доступна для бронирования");
        }

        Booking booking = bookingMapper.toBooking(bookingRequestDto, item, booker, Status.WAITING);

        ItemDto itemDto = booking.getItem() != null ?
                itemMapper.toItemDto(booking.getItem()) : null;

        return bookingMapper.toBookingDto(bookingRepository.save(booking), itemDto);
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
        ItemDto itemDto = booking.getItem() != null ?
                itemMapper.toItemDto(booking.getItem()) : null;
        return bookingMapper.toBookingDto(bookingRepository.save(booking), itemDto);
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
        ItemDto itemDto = booking.getItem() != null ?
                itemMapper.toItemDto(booking.getItem()) : null;
        return bookingMapper.toBookingDto(booking, itemDto);
    }

    @Override
    public Collection<BookingDto> getAllBookings(String state, Long userId) {
        getUserIfExists(userId);
        try {
            BookingState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверный статус для поиска " + state);
        }

        return bookingRepository.findAll(state, userId)
                .stream()
                .map(booking -> bookingMapper.toBookingDto(booking, itemMapper.toItemDto(booking.getItem())))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<BookingDto> findBookingsForOwnerItems(String bookingState, Long userId) {
        getUserIfExists(userId);
        try {
            BookingState.valueOf(bookingState.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверный статус для поиска " + bookingState);
        }

        return bookingRepository.getBookingsForOwnerItems(bookingState, userId)
                .stream()
                .map(booking -> bookingMapper.toBookingDto(booking, itemMapper.toItemDto(booking.getItem())))
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
