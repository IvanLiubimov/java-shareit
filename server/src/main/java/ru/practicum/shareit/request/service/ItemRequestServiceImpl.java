package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoToRequest;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

   private final UserRepository userRepository;
   private final ItemRepository itemRepository;
   private final ItemRequestMapper itemRequestMapper;
   private final ItemRequestRepository itemRequestRepository;
   private final ItemMapper itemMapper;

    @Override
    public ItemRequestDto createItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        User user = getUserIfExists(userId);
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto, user);
        ItemRequest createdItemRequest = itemRequestRepository.save(itemRequest);
        List<ItemDtoToRequest> listOfItemDtoToRequest = Optional.ofNullable(createdItemRequest.getItemsList())
                .orElse(Collections.emptyList())
                .stream()
                .map(itemMapper::toItemDtoToRequest)
                .toList();
        return itemRequestMapper.toItemRequestDto(createdItemRequest, listOfItemDtoToRequest);
    }

    @Override
    public Collection<ItemRequestDto> getAllItemRequestsOfUser(Long userId) {
        User user = getUserIfExists(userId);
        Collection<ItemRequest> itemRequestsOfUser = itemRequestRepository.findAllRequestsByUserId(userId);
        itemRequestsOfUser.stream().toList();
        return convertRequestListToDto(itemRequestsOfUser);

    }

    @Override
    public Collection<ItemRequestDto> getAllItemRequests(Long userId) {
        Collection<ItemRequest> allItemRequests = itemRequestRepository.findAll(userId);
        return convertRequestListToDto(allItemRequests);
    }

    @Override
    public ItemRequestDto getItemRequestById(Long requestId) {

         ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                 .orElseThrow(() -> new NotFoundException("Запрос с id = " + requestId + " не найден"));
         List<ItemDtoToRequest> itemDtoToRequests = Optional.ofNullable(itemRequest.getItemsList())
                 .orElse(Collections.emptyList())
                 .stream()
                 .map(itemMapper::toItemDtoToRequest)
                 .toList();
         return itemRequestMapper.toItemRequestDto(itemRequest, itemDtoToRequests);
    }

    private User getUserIfExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
    }

    private Item getItemIfExists(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + " не найдена"));
    }

    private Collection<ItemRequestDto> convertRequestListToDto(Collection<ItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(itemRequest -> itemRequestMapper.toItemRequestDto(
                        itemRequest,
                        itemRequest.getItemsList()
                                .stream()
                                .map(item -> itemMapper.toItemDtoToRequest(item))
                                .toList()
                )).toList();
    }
}
