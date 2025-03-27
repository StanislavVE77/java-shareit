package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
public class ItemRequestControllerTest {
    @MockBean
    private ItemRequestService requestService;

    @Autowired
    private MockMvc mockMvc;

    public ObjectMapper objectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return objectMapper;
    }

    private final ItemRequestMapper requestMapper = new ItemRequestMapper();

    User user1 = new User(2L, "Username2", "user2@shareit.ru");
    User user2 = new User(3L, "Username3", "user3@shareit.ru");
    User user3 = new User(4L, "Username4", "user4@shareit.ru");
    ItemRequest newRequest = new ItemRequest(1L, "Request description 1", Instant.parse("2025-03-01T18:22:23.00Z"), user1);
    ItemRequest request1 = new ItemRequest(2L, "Request description 2", Instant.parse("2025-03-01T18:22:23.00Z"), user2);
    ItemRequest request2 = new ItemRequest(3L, "Request description 3", Instant.parse("2025-03-03T00:00:00.00Z"), user3);
    ItemRequest request3 = new ItemRequest(4L, "Request description 4", Instant.parse("2025-03-04T00:00:00.00Z"), user1);
    Item item1 = new Item(2L, "Item2", "Description2", true, user1, 2L);
    Item item2 = new Item(3L, "Item3", "Описание", false, user1, null);
    Item item3 = new Item(4L, "Item4", "Description4", true, user1, null);
    ItemRequestDto itemRequestDto1 = requestMapper.toItemRequestDto(request1);
    ItemRequestDto itemRequestDto2 = requestMapper.toItemRequestDto(request2);
    ItemRequestDto itemRequestDto3 = requestMapper.toItemRequestDto(request3);
    List<Item> items = List.of(item1, item2, item3);
    ItemRequestWithItemsDto requestWithItems = requestMapper.toItemRequestWithItemsDto(request3, items);
    ItemRequestDto createdRequestDto = requestMapper.toItemRequestDto(newRequest);

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
    }

    @Test
    @Order(1)
    void getItemRequest() throws Exception {
        long userId = 2L;
        long requestId = 4L;

        String jsonItemRequest = objectMapper().writeValueAsString(requestWithItems);

        when(requestService.getItemRequest(userId, requestId)).thenReturn(requestWithItems);

        mockMvc.perform(get("/requests/" + requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonItemRequest));

        verify(requestService, times(1)).getItemRequest(userId, requestId);
    }

    @Test
    @Order(2)
    void createItemRequest() throws Exception {
        long userId = 2L;

        ItemRequestCreateDto newItemRequestDto = new ItemRequestCreateDto(1L, "Request description 1");
        String jsonNewItemRequest = objectMapper().writeValueAsString(createdRequestDto);

        when(requestService.createItemRequest(userId, newItemRequestDto)).thenReturn(createdRequestDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper().writeValueAsString(newItemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(newItemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(newItemRequestDto.getDescription())));

        verify(requestService, times(1)).createItemRequest(userId, newItemRequestDto);
    }

    @Test
    @Order(3)
    void getUserItemRequests() throws Exception {
        long userId = 2L;

        List<ItemRequestWithItemsDto> itemRequests = List.of(requestWithItems);
        String jsonItemRequests = objectMapper().writeValueAsString(itemRequests);

        when(requestService.getUserItemRequests(userId)).thenReturn(itemRequests);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonItemRequests));

        verify(requestService, times(1)).getUserItemRequests(userId);
    }

    @Test
    @Order(4)
    void getOtherUserItemRequests() throws Exception {
        long userId = 2L;

        List<ItemRequestDto> itemRequests = List.of(itemRequestDto2, itemRequestDto3, createdRequestDto);
        String jsonItemRequests = objectMapper().writeValueAsString(itemRequests);

        when(requestService.getOtherUsersItemRequests(userId)).thenReturn(itemRequests);

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonItemRequests));

        verify(requestService, times(1)).getOtherUsersItemRequests(userId);

    }
}
