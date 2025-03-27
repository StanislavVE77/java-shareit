package ru.practicum.shareit.item;

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
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
public class ItemControllerTest {
    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    public ObjectMapper objectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return objectMapper;
    }

    private final ItemMapper itemMapper = new ItemMapper();
    private final CommentMapper commentMapper = new CommentMapper();

    User user1 = new User(2L, "Username2", "user2@shareit.ru");
    User user2 = new User(3L, "Username3", "user3@shareit.ru");
    User user3 = new User(4L, "Username4", "user4@shareit.ru");
    Item newItem = new Item(1L, "Item1", "Description1", false, user1, 2L);
    Item updateItem = new Item(2L, "Item2Updated", "Description2 Updated", false, user1, 2L);
    Item item1 = new Item(2L, "Item2", "Description2", true, user1, 2L);
    Item item2 = new Item(3L, "Item3", "Описание", false, user1, null);
    Item item3 = new Item(4L, "Item4", "Description4", true, user1, null);
    Comment newComment = new Comment(1L, "comment1", item2, user1, Instant.parse("2025-03-09T00:00:01.00Z"));
    Comment comment1 = new Comment(2L, "comment2", item1, user2, Instant.parse("2025-03-10T00:00:01.00Z"));
    Comment comment2 = new Comment(3L, "comment3", item2, user2, Instant.parse("2025-03-11T00:00:01.00Z"));
    Comment comment3 = new Comment(4L, "comment4", item2, user1, Instant.parse("2025-03-12T00:00:01.00Z"));
    Booking booking1 = new Booking(2L, LocalDateTime.of(2025, 3, 28, 0, 0, 1), LocalDateTime.of(2025, 4, 1, 0, 0, 1), item1, user1, BookingStatus.WAITING);
    Booking booking2 = new Booking(3L, LocalDateTime.of(2025, 1, 1, 0, 0, 1), LocalDateTime.of(2025, 2, 1, 0, 0, 1), item1, user2, BookingStatus.CANCELED);

    ItemDto itemDto1 = itemMapper.toItemDto(item1);
    ItemDto itemDto2 = itemMapper.toItemDto(item2);
    ItemDto itemDto3 = itemMapper.toItemDto(item3);
    ItemDto createdItemDto = itemMapper.toItemDto(newItem);
    ItemDto updatedItemDto = itemMapper.toItemDto(updateItem);
    CommentDto commentDto1 = commentMapper.toCommentDto(comment1);
    List<CommentDto> comments = List.of(commentDto1);
    List<Booking> bookings = List.of(booking1, booking2);
    ItemBookingDto itemBookingDto1 = itemMapper.toItemBookingDto(item1, bookings, comments);
    CommentDto createdCommentDto = commentMapper.toCommentDto(newComment);

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();

    }

    @Test
    @Order(1)
    void getItem() throws Exception {

        String jsonItem1 = objectMapper().writeValueAsString(itemDto1);

        when(itemService.getItem(itemDto1.getId())).thenReturn(itemBookingDto1);

        mockMvc.perform(get("/items/" + itemDto1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonItem1));

        verify(itemService, times(1)).getItem(itemDto1.getId());
    }

    @Test
    @Order(2)
    void getItemsByUser() throws Exception {
        long userId = 2L;

        List<ItemDto> items = List.of(itemDto1, itemDto2, itemDto3);
        String jsonItems = objectMapper().writeValueAsString(items);

        when(itemService.getItemsByUser(userId)).thenReturn(items);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonItems));

        verify(itemService, times(1)).getItemsByUser(userId);
    }

    @Test
    @Order(3)
    void createItem() throws Exception {
        long userId = 2L;

        ItemCreateDto newItemDto = new ItemCreateDto("Item1", "Description1", false, 2L);
        String jsonNewItem = objectMapper().writeValueAsString(createdItemDto);

        when(itemService.createItem(userId, newItemDto)).thenReturn(createdItemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper().writeValueAsString(newItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonNewItem));

        verify(itemService, times(1)).createItem(userId, newItemDto);
    }

    @Test
    @Order(4)
    void updateItem() throws Exception {
        long userId = 2L;
        long itemId = 2L;

        ItemUpdateDto updateItemDto = new ItemUpdateDto(itemId, "Item2Updated", "Description2 Updated", false, 2L);
        String jsonUpdateItem = objectMapper().writeValueAsString(updatedItemDto);

        when(itemService.updateItem(userId, updateItemDto)).thenReturn(updatedItemDto);

        mockMvc.perform(patch("/items/" + itemId)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper().writeValueAsString(updateItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonUpdateItem));

        verify(itemService, times(1)).updateItem(userId, updateItemDto);
    }

    @Test
    @Order(5)
    void searchItems() throws Exception {
        long userId = 2L;
        String text = "%Description%";

        List<ItemDto> items = List.of(itemDto1, createdItemDto, itemDto3);
        String jsonItems = objectMapper().writeValueAsString(items);

        when(itemService.searchItems(text, userId)).thenReturn(items);

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", text))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonItems));

        verify(itemService, times(1)).searchItems(text, userId);
    }

    @Test
    @Order(6)
    void createComment() throws Exception {
        long userId = 2L;
        long itemId = 3L;

        CommentCreateDto newCommentDto = new CommentCreateDto("comment1");
        String jsonNewComment = objectMapper().writeValueAsString(createdCommentDto);

        when(itemService.createComment(itemId, userId, newCommentDto)).thenReturn(createdCommentDto);

        mockMvc.perform(post("/items/" + itemId + "/comment")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper().writeValueAsString(newCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonNewComment));

        verify(itemService, times(1)).createComment(itemId, userId, newCommentDto);
    }
}
