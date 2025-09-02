package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemDtoToRequest;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.*;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTests {

    @InjectMocks
    ItemRequestServiceImpl itemRequestService;

    @Mock
    ItemRequestRepository itemRequestRepository;

    @Mock
    ItemRequestMapper itemRequestMapper;

    @Mock
    ItemMapper itemMapper;

    @Mock
    UserRepository userRepository;

    @Test
    void testCreateItemRequestWhenUserExists() throws Exception {
        User user = new User(3L, "John Watson", "JohnWatson@gmail.com");
        List<Item> items = new ArrayList<>();
        List<ItemDtoToRequest> emptyDtoList = Collections.emptyList();
        ItemRequestDto itemRequestDto = new ItemRequestDto(3L, "Нужна дрель",
                LocalDateTime.of(2025, Month.AUGUST, 15, 12, 5), null);
        ItemRequest itemRequest = new ItemRequest(3L, "Нужна дрель",
                user,
                itemRequestDto.getDateTime().atZone(ZoneId.systemDefault()).toInstant(),
                items);
        ItemRequestDto expectedDto = new ItemRequestDto(3L, "Нужна дрель",
                LocalDateTime.of(2025, Month.AUGUST, 15, 12, 5), Collections.emptyList());


        Mockito.when(userRepository.findById(3L)).thenReturn(Optional.of(user));
        Mockito.when(itemRequestMapper.toItemRequest(itemRequestDto, user)).thenReturn(itemRequest);
        Mockito.when(itemRequestRepository.save(itemRequest)).thenReturn(itemRequest);
        Mockito.when(itemRequestMapper.toItemRequestDto(itemRequest, emptyDtoList)).thenReturn(expectedDto);

        ItemRequestDto result = itemRequestService.createItemRequest(user.getId(), itemRequestDto);

        Assertions.assertEquals(result, expectedDto);

        Mockito.verify(userRepository, times(1)).findById(3L);
        Mockito.verify(itemRequestMapper, times(1)).toItemRequest(itemRequestDto, user);
        Mockito.verify(itemRequestRepository, times(1)).save(itemRequest);
        Mockito.verify(itemRequestMapper, times(1)).toItemRequestDto(itemRequest, emptyDtoList);
    }

    @Test
    void testGetAllItemRequestsOfUser() throws Exception {
        User user1 = new User(3L, "John Watson", "JohnWatson@gmail.com");
        Item item1 = new Item(1L, "Дрель", "набор бит",
                true, user1, null);
        Item item2 = new Item(2L, "Шуруповерт", "Мощный",
                true, user1, null);
        Item item3 = new Item(3L, "Перфоратор", "Перфоратор",
                true, user1, null);
        List<Item> items1 = new ArrayList<>();
        List<Item> items2 = new ArrayList<>();

        ItemDtoToRequest itemDtoToRequest1 = new ItemDtoToRequest(1L, "Дрель", 3L);
        ItemDtoToRequest itemDtoToRequest2 = new ItemDtoToRequest(2L, "Шуруповерт", 3L);
        ItemDtoToRequest itemDtoToRequest3 = new ItemDtoToRequest(3L, "Перфоратор", 3L);
        List<ItemDtoToRequest> itemDtoToRequests1 = new ArrayList<>();
        List<ItemDtoToRequest> itemDtoToRequests2 = new ArrayList<>();
        itemDtoToRequests1.add(itemDtoToRequest1);
        itemDtoToRequests2.add(itemDtoToRequest2);
        itemDtoToRequests2.add(itemDtoToRequest3);

        ItemRequestDto itemRequestDto1 = new ItemRequestDto(3L, "Нужна дрель",
                LocalDateTime.of(2025, Month.AUGUST, 15, 12, 5), itemDtoToRequests1);
        ItemRequestDto itemRequestDto2 = new ItemRequestDto(4L, "Нужен скотч",
                LocalDateTime.of(2025, Month.AUGUST, 12, 17, 40), itemDtoToRequests2);
        ItemRequest itemRequest1 = new ItemRequest(3L, "Нужна дрель",
                user1,
                itemRequestDto1.getDateTime().atZone(ZoneId.systemDefault()).toInstant(),
                items1);
        ItemRequest itemRequest2 = new ItemRequest(4L, "Нужен скотч",
                user1,
                itemRequestDto2.getDateTime().atZone(ZoneId.systemDefault()).toInstant(),
                items2);

        Collection<ItemRequest> itemRequestsOfUser1 = new ArrayList<>();
        itemRequestsOfUser1.add(itemRequest1);
        itemRequestsOfUser1.add(itemRequest2);

        item1.setRequest(itemRequest1);
        item2.setRequest(itemRequest1);
        item3.setRequest(itemRequest2);
        items1.add(item1);
        items1.add(item2);
        items2.add(item3);


        Mockito.when(userRepository.findById(3L)).thenReturn(Optional.of(user1));
        Mockito.when(itemRequestRepository.findAllRequestsByUserId(3L)).thenReturn(itemRequestsOfUser1);
        Mockito.when(itemMapper.toItemDtoToRequest(item1)).thenReturn(itemDtoToRequest1);
        Mockito.when(itemMapper.toItemDtoToRequest(item2)).thenReturn(itemDtoToRequest2);
        Mockito.when(itemMapper.toItemDtoToRequest(item3)).thenReturn(itemDtoToRequest3);
        Mockito.when(itemRequestMapper.toItemRequestDto(Mockito.eq(itemRequest1), Mockito.anyList()))
                .thenReturn(itemRequestDto1);
        Mockito.when(itemRequestMapper.toItemRequestDto(Mockito.eq(itemRequest2), Mockito.anyList()))
                .thenReturn(itemRequestDto2);

        Collection<ItemRequestDto> result = itemRequestService.getAllItemRequestsOfUser(user1.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        List<ItemRequestDto> resultList = new ArrayList<>(result);
        Assertions.assertTrue(resultList.contains(itemRequestDto1));
        Assertions.assertTrue(resultList.contains(itemRequestDto2));

        Mockito.verify(itemRequestRepository, times(1)).findAllRequestsByUserId(3L);
    }

    @Test
    void testGetAllItemRequests() {
        User user1 = new User(3L, "John Watson", "JohnWatson@gmail.com");
        User user2 = new User(4L, "Pete Watson", "PeteWatson@gmail.com");

        Item item1 = new Item(1L, "Дрель", "набор бит", true, user1, null);
        Item item2 = new Item(2L, "Шуруповерт", "Мощный", true, user1, null);
        Item item3 = new Item(3L, "Перфоратор", "Перфоратор", true, user1, null);

        List<Item> items1 = new ArrayList<>();
        List<Item> items2 = new ArrayList<>();

        ItemDtoToRequest itemDtoToRequest1 = new ItemDtoToRequest(1L, "Дрель", 3L);
        ItemDtoToRequest itemDtoToRequest2 = new ItemDtoToRequest(2L, "Шуруповерт", 3L);
        ItemDtoToRequest itemDtoToRequest3 = new ItemDtoToRequest(3L, "Перфоратор", 3L);

        List<ItemDtoToRequest> itemDtoToRequests1 = new ArrayList<>();
        List<ItemDtoToRequest> itemDtoToRequests2 = new ArrayList<>();
        itemDtoToRequests1.add(itemDtoToRequest1);
        itemDtoToRequests2.add(itemDtoToRequest2);
        itemDtoToRequests2.add(itemDtoToRequest3);

        ItemRequestDto itemRequestDto1 = new ItemRequestDto(3L, "Нужна дрель",
                LocalDateTime.of(2025, Month.AUGUST, 15, 12, 5), itemDtoToRequests1);
        ItemRequestDto itemRequestDto2 = new ItemRequestDto(4L, "Нужен скотч",
                LocalDateTime.of(2025, Month.AUGUST, 12, 17, 40), itemDtoToRequests2);

        ItemRequest itemRequest1 = new ItemRequest(3L, "Нужна дрель", user1,
                itemRequestDto1.getDateTime().atZone(ZoneId.systemDefault()).toInstant(), items1);
        ItemRequest itemRequest2 = new ItemRequest(4L, "Нужен скотч", user2,
                itemRequestDto2.getDateTime().atZone(ZoneId.systemDefault()).toInstant(), items2);

        item1.setRequest(itemRequest1);
        item2.setRequest(itemRequest1);
        item3.setRequest(itemRequest2);
        items1.add(item1);
        items1.add(item2);
        items2.add(item3);

        List<ItemRequest> allItemRequest = List.of(itemRequest2);

        Mockito.when(itemRequestRepository.findAll(anyLong())).thenReturn(allItemRequest);
        Mockito.when(itemMapper.toItemDtoToRequest(any(Item.class)))
                .thenReturn(itemDtoToRequest1)
                .thenReturn(itemDtoToRequest2)
                .thenReturn(itemDtoToRequest3);
        Mockito.when(itemMapper.toItemDtoToRequest(item3)).thenReturn(itemDtoToRequest3);
        Mockito.when(itemRequestMapper.toItemRequestDto(Mockito.eq(itemRequest2), Mockito.anyList()))
                .thenReturn(itemRequestDto2);

        Collection<ItemRequestDto> result = itemRequestService.getAllItemRequests(user1.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());

        List<ItemRequestDto> resultList = new ArrayList<>(result);
        Assertions.assertFalse(resultList.contains(itemRequestDto1));
        Assertions.assertTrue(resultList.contains(itemRequestDto2));

        Mockito.verify(itemRequestRepository, times(1)).findAll(anyLong());
        Mockito.verify(itemMapper, times(1)).toItemDtoToRequest(any(Item.class));
        Mockito.verify(itemRequestMapper, times(0)).toItemRequestDto(Mockito.eq(itemRequest1), Mockito.anyList());
        Mockito.verify(itemRequestMapper, times(1)).toItemRequestDto(Mockito.eq(itemRequest2), Mockito.anyList());
    }

    @Test
    void getItemRequestById_whenRequestExists_shouldReturnRequestDto() {
        Long requestId = 1L;
        LocalDateTime created = LocalDateTime.now();

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(requestId);
        itemRequest.setDescription("Нужна дрель");
        itemRequest.setCreated(created.atZone(ZoneId.systemDefault()).toInstant());

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Дрель");
        item1.setRequest(itemRequest);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Молоток");
        item1.setRequest(itemRequest);

        itemRequest.setItemsList(List.of(item1, item2));
        ItemDtoToRequest itemDto1 = new ItemDtoToRequest(1L, "Дрель", 1L);
        ItemDtoToRequest itemDto2 = new ItemDtoToRequest(2L, "Молоток", 2L);
        List<ItemDtoToRequest> itemDtos = List.of(itemDto1, itemDto2);

        ItemRequestDto expectedDto = new ItemRequestDto();
        expectedDto.setId(requestId);
        expectedDto.setDescription("Нужна дрель");
        expectedDto.setDateTime(created);
        expectedDto.setItemsList(itemDtos);

        Mockito.when(itemRequestRepository.findById(requestId))
                .thenReturn(Optional.of(itemRequest));
        Mockito.when(itemMapper.toItemDtoToRequest(item1))
                .thenReturn(itemDto1);
        Mockito.when(itemMapper.toItemDtoToRequest(item2))
                .thenReturn(itemDto2);
        Mockito.when(itemRequestMapper.toItemRequestDto(itemRequest, itemDtos))
                .thenReturn(expectedDto);

        ItemRequestDto result = itemRequestService.getItemRequestById(requestId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(requestId, result.getId());
        Assertions.assertEquals("Нужна дрель", result.getDescription());
        Assertions.assertEquals(created, result.getDateTime());
        Assertions.assertNotNull(result.getItemsList());
        Assertions.assertEquals(2, result.getItemsList().size());
        Assertions.assertEquals("Дрель", result.getItemsList().get(0).getName());

        Mockito.verify(itemRequestRepository, times(1)).findById(requestId);
    }





}
