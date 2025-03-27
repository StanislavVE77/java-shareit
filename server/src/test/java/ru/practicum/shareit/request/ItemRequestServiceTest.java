package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class ItemRequestServiceTest {
    private final EntityManager em;
    private final ItemRequestService service;
    private final ItemRequestMapper mapper = new ItemRequestMapper();

    User user1 = new User(2L, "Username2", "user2@shareit.ru");
    User user2 = new User(3L, "Username3", "user3@shareit.ru");
    User user3 = new User(4L, "Username4", "user4@shareit.ru");
    ItemRequest request1 = new ItemRequest(2L, "Request description 2", Instant.parse("2025-03-01T18:22:23.00Z"), user2);
    ItemRequest request2 = new ItemRequest(3L, "Request description 3", Instant.parse("2025-03-03T00:00:00.00Z"), user3);
    ItemRequest request3 = new ItemRequest(4L, "Request description 4", Instant.parse("2025-03-04T00:00:00.00Z"), user1);

    ItemRequestDto requestDto1 = mapper.toItemRequestDto(request1);
    ItemRequestDto requestDto2 = mapper.toItemRequestDto(request2);
    ItemRequestDto requestDto3 = mapper.toItemRequestDto(request3);

    List<ItemRequestDto> getItemRequests() {
        return List.of(requestDto1, requestDto2, requestDto3);
    }

    @Test
    @Order(1)
    void getItemRequest() {
        long userId = 3L;

        ItemRequestWithItemsDto itemsRequest = service.getItemRequest(userId, requestDto1.getId());

        assertEquals(requestDto1.getDescription(), itemsRequest.getDescription());
        assertEquals(requestDto1.getRequestor(), itemsRequest.getRequestor());


        long userNotFoundId = 5L;
        assertThrows(NotFoundException.class, () -> {
            service.getItemRequest(userNotFoundId, requestDto1.getId());
        });

    }

    @Test
    @Order(2)
    void getUserItemRequests() {
        long userId = 3L;

        List<ItemRequestWithItemsDto> itemRequests = service.getUserItemRequests(userId);

        assertEquals(1, itemRequests.size());

        long userNotFoundId = 5L;
        assertThrows(NotFoundException.class, () -> {
            service.getUserItemRequests(userNotFoundId);
        });

    }

    @Test
    @Order(3)
    void getOtherUsersItemRequests() {
        long userId = 2L;

        List<ItemRequestDto> itemRequests = service.getOtherUsersItemRequests(userId);

        assertEquals(3, itemRequests.size());

        long userNotFoundId = 5L;
        assertThrows(NotFoundException.class, () -> {
            service.getOtherUsersItemRequests(userNotFoundId);
        });

    }

    @Test
    @Order(4)
    void createItemRequest() {
        List<ItemRequestDto> itemRequests = service.getAllItemRequests();

        assertEquals(getItemRequests().size(), itemRequests.size());

        ItemRequestCreateDto newCreateItemRequest = new ItemRequestCreateDto(1L, "Description1");
        ItemRequestDto newItemRequest = service.createItemRequest(2L, newCreateItemRequest);

        assertEquals(getItemRequests().size() + 1, service.getAllItemRequests().size());

        long userNotFoundId = 5L;
        assertThrows(NotFoundException.class, () -> {
            service.createItemRequest(userNotFoundId, newCreateItemRequest);
        });

    }
}
