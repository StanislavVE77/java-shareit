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
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;

import java.nio.charset.StandardCharsets;
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

    private final TestData testData = new TestData();

    private final ItemMapper itemMapper = new ItemMapper();
    private final CommentMapper commentMapper = new CommentMapper();

    ItemDto itemDto1;
    ItemDto itemDto2;
    ItemDto itemDto3;
    ItemDto createdItemDto;
    ItemDto updatedItemDto;
    ItemBookingDto itemBookingDto1;
    CommentDto commentDto1;
    CommentDto createdCommentDto;
    List<CommentDto> comments;
    List<Booking> bookings;

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();

        itemDto1 = itemMapper.toItemDto(testData.item1);
        itemDto2 = itemMapper.toItemDto(testData.item2);
        itemDto3 = itemMapper.toItemDto(testData.item3);
        commentDto1 = commentMapper.toCommentDto(testData.comment1);
        comments = List.of(commentDto1);
        bookings = List.of(testData.booking1, testData.booking2);
        itemBookingDto1 = itemMapper.toItemBookingDto(testData.item1, bookings, comments);
        createdItemDto = itemMapper.toItemDto(testData.newItem);
        updatedItemDto = itemMapper.toItemDto(testData.updateItem);
        createdCommentDto = commentMapper.toCommentDto(testData.newComment);

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
