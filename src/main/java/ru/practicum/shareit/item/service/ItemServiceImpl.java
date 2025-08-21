package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemValidator itemValidator;
    private final ItemMapper itemMapper;
    private final CommentValidator commentValidator;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public Collection<ItemDtoAll> getAllItems(Long userId) {
        getUserIfExists(userId);

        Collection<Item> items = itemRepository.findAllByOwnerId(userId);
        Collection<Long> itemIds = items.stream()
                .map(Item::getId)
                .toList();

        Collection<CommentDto> comments = commentRepository.findAllByItemIdIn(itemIds)
                .stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());


        Map<Long, Booking> lastBookings = bookingRepository.findLastBookings(itemIds).stream()
                .collect(Collectors.toMap(b -> b.getItem().getId(), b -> b));

        Map<Long, Booking> nextBookings = bookingRepository.findNextBookings(itemIds).stream()
                .collect(Collectors.toMap(b -> b.getItem().getId(), b -> b));

        return items.stream()
                .map(item -> itemMapper.toItemDtoAll(
                        item,
                        comments,
                        lastBookings.get(item.getId()),
                        nextBookings.get(item.getId())
                ))
                .toList();
    }

    @Override
    public ItemDtoAll getItemById(Long id, Long userId) {
        Item item = getItemIfExists(id);
        Collection<CommentDto> comments = commentRepository.findAllByItemId(id)
                .stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());

        List<Booking> bookings = bookingRepository.findAllByItemId(item.getId()).stream().toList();

        Long itemOwnerId = bookings.isEmpty() ? null : bookings.getFirst().getItem().getOwner().getId();

        Booking lastBooking = null;
        Booking nearestBooking = null;

        if (userId.equals(itemOwnerId)) {
            lastBooking = bookings.stream()
                    .filter(b -> b.getStart().isBefore(Instant.now()))
                    .max(Comparator.comparing(Booking::getEnd))
                    .orElse(null);

            nearestBooking = bookings.stream()
                    .filter(b -> b.getStart().isAfter(Instant.now()))
                    .min(Comparator.comparing(Booking::getStart))
                    .orElse(null);
        }

        return itemMapper.toItemDtoAll(item, comments, lastBooking, nearestBooking);
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        User owner = getUserIfExists(userId);

        itemValidator.validate(itemDto);

        Item item = itemMapper.toItem(itemDto, owner);
        Item createdItem = itemRepository.save(item);
        return itemMapper.toItemDto(createdItem);
    }

    public ItemDto editItem(Long itemId, ItemDto itemDto, Long userId) {
        itemValidator.validateForUpdate(itemDto);
        getUserIfExists(userId);
        Item oldItem = getItemIfExists(itemId);

        if (!oldItem.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Вещь с id = " + itemDto.getId() + " не найдена или не принадлежит пользователю");
        }

        itemMapper.updateItemFromDto(itemDto, oldItem);
        Item updatedItem = itemRepository.save(oldItem);

        return itemMapper.toItemDto(updatedItem);
    }

    public Collection<ItemDto> searchItems(String query, Long userId) {
        getUserIfExists(userId);
        return itemRepository.searchItem(query).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(Long itemId, Long userId, CommentDto commentDto) {

        commentValidator.validate(commentDto);
        Item item = getItemIfExists(itemId);
        User user = getUserIfExists(userId);

        boolean isUserBookedItem = bookingRepository.existsByItemIdAndBookerIdAndEndBefore(
                itemId,
                userId,
                Instant.now()
        );

        if (!isUserBookedItem) {
                    throw new ConditionsNotMetException("Пользователь с id " + userId +
                            " не брал в аренду вещь " + itemId +
                            " и не может оставить к ней комментарий");
                }

        Comment comment = commentMapper.toComment(commentDto, item, user);

        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    private User getUserIfExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
    }

    private Item getItemIfExists(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + " не найдена"));
    }

}
