package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;


import java.time.ZoneId;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    private final BookingRepository bookingRepository;

    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner() != null ? item.getOwner().getId() : null
        );
    }

    public Item toItem(ItemDto itemDto, User owner) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);
        item.setRequest(null);
        return item;
    }

    public ItemDtoAll toItemDtoAll(Item item,
                                   Collection<CommentDto> comments,
                                   Booking lastBooking,
                                   Booking nextBooking) {
        return new ItemDtoAll(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                item.getOwner() != null ? item.getOwner().getId() : null,
                lastBooking != null ?
                        lastBooking.getEnd().atZone(ZoneId.systemDefault()).toLocalDate() : null,
                nextBooking != null ?
                        nextBooking.getStart().atZone(ZoneId.systemDefault()).toLocalDate() : null,
                comments
        );
    }

        public void updateItemFromDto(ItemDto itemDto, Item item) {
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
    }

}
