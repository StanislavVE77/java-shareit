package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;

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
    private final TestData testData = new TestData();

    ItemRequestDto request1 = mapper.toItemRequestDto(testData.request1);
    ItemRequestDto request2 = mapper.toItemRequestDto(testData.request2);
    ItemRequestDto request3 = mapper.toItemRequestDto(testData.request3);

    List<ItemRequestDto> getItemRequests() {
        return List.of(request1, request2, request3);
    }

    @Test
    @Order(1)
    void getItemRequest() {
        long userId = 3L;

        ItemRequestWithItemsDto itemsRequest = service.getItemRequest(userId, request1.getId());

        assertEquals(request1.getDescription(), itemsRequest.getDescription());
        assertEquals(request1.getRequestor(), itemsRequest.getRequestor());


        long userNotFoundId = 5L;
        assertThrows(NotFoundException.class, () -> {
            service.getItemRequest(userNotFoundId, request1.getId());
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
