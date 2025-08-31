package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

@Service
public interface ItemRequestService {

    ItemRequestDto createItemRequest(Long userId, ItemRequestDto itemRequestDto);

    Collection<ItemRequestDto> getAllItemRequestsOfUser(Long userId);

    Collection<ItemRequestDto> getAllItemRequests();

    ItemRequestDto getItemRequestById(Long requestId);
}
