package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class ItemServiceTest {
    private final EntityManager em;
    private final ItemService service;
    private final ItemMapper itemMapper = new ItemMapper();
    private final CommentMapper commentMapper = new CommentMapper();

    User user1 = new User(2L, "Username2", "user2@shareit.ru");
    User user2 = new User(3L, "Username3", "user3@shareit.ru");
    User user3 = new User(4L, "Username4", "user4@shareit.ru");
    Item item1 = new Item(2L, "Item2", "Description2", true, user1, 2L);
    Item item2 = new Item(3L, "Item3", "Описание", false, user1, null);
    Item item3 = new Item(4L, "Item4", "Description4", true, user1, null);
    Comment comment1 = new Comment(2L, "comment2", item1, user2, Instant.parse("2025-03-10T00:00:01.00Z"));
    Comment comment2 = new Comment(3L, "comment3", item2, user2, Instant.parse("2025-03-11T00:00:01.00Z"));
    Comment comment3 = new Comment(4L, "comment4", item2, user1, Instant.parse("2025-03-12T00:00:01.00Z"));

    ItemDto itemDto1 = itemMapper.toItemDto(item1);
    ItemDto itemDto2 = itemMapper.toItemDto(item2);
    ItemDto itemDto3 = itemMapper.toItemDto(item3);
    CommentDto commentDto1 = commentMapper.toCommentDto(comment1);
    CommentDto commentDto2 = commentMapper.toCommentDto(comment2);
    CommentDto commentDto3 = commentMapper.toCommentDto(comment3);
    List<CommentDto> comments = List.of(commentDto1, commentDto2);

    BookingShortDto booking2 = new BookingShortDto(2L, LocalDateTime.of(2025, 1, 1, 0, 0, 1),
            LocalDateTime.of(2025, 2, 1, 0, 0, 1));
    BookingShortDto booking1 = new BookingShortDto(1L, LocalDateTime.of(2025, 3, 28, 0, 0, 1),
            LocalDateTime.of(2025, 4, 1, 0, 0, 1));

    ItemBookingDto itemBooking1 = new ItemBookingDto(1L, "Item2", "Description2", true, comments, booking2, booking1);

    List<ItemDto> getItems() {
        return List.of(itemDto1, itemDto2, itemDto3);
    }

    List<CommentDto> getComments() {
        return List.of(commentDto1, commentDto2, commentDto3);
    }

    @Test
    @Order(1)
    void getItem() {
        long itemId = itemDto2.getId();

        ItemBookingDto item = service.getItem(itemId);

        assertEquals(itemDto2.getId(), item.getId());

    }

    @Test
    @Order(2)
    void getItemsByUser() {
        long userId = 2L;
        long userNotExistId = 5L;

        List<ItemDto> items = service.getItemsByUser(userId);

        assertEquals(getItems(),  items);

        assertThrows(NotFoundException.class, () -> {
            service.getItemsByUser(userNotExistId);
        });

    }

    @Test
    @Order(3)
    void createItem() {
        List<ItemDto> items = service.getAllItems();

        assertEquals(getItems().size(), items.size());

        ItemCreateDto newCreateItem = new ItemCreateDto("Item1", "Description1", false, 2L);

        try {
            ItemDto newItem = service.createItem(5L, newCreateItem);
            assertEquals(getItems().size() + 1, service.getAllItems().size());
        } catch (Exception e) {
            assertNotEquals(getItems().size() + 1, service.getAllItems().size());
        }

        ItemDto newItem = service.createItem(2L, newCreateItem);

        assertEquals(getItems().size() + 1, service.getAllItems().size());
    }

    @Test
    @Order(4)
    void updateItem() {
        long userId = 2L;
        long userNotExistId = 5L;

        ItemUpdateDto updateItem = new ItemUpdateDto(2L, "Item2Updated","Description2  Updated", false, 1L);
        ItemDto item = service.updateItem(userId, updateItem);

        ItemBookingDto itemBookingDto = service.getItem(2L);

        assertNotEquals(itemDto1.getName(), itemBookingDto.getName());
        assertEquals(updateItem.getName(), itemBookingDto.getName());
        assertNotEquals(itemDto1.getDescription(), itemBookingDto.getDescription());
        assertEquals(updateItem.getDescription(), itemBookingDto.getDescription());
        assertNotEquals(itemDto1.getAvailable(), itemBookingDto.getAvailable());
        assertEquals(updateItem.getAvailable(), itemBookingDto.getAvailable());

        assertThrows(NotFoundException.class, () -> {
            service.updateItem(userNotExistId, updateItem);
        });

        ItemUpdateDto updateItem2 = new ItemUpdateDto(5L, "Item5Updated","Description5  Updated", false, 1L);
        assertThrows(NotFoundException.class, () -> {
            service.updateItem(userId, updateItem2);
        });

    }

    @Test
    @Order(5)
    void searchItems() {
        long userId = 2L;
        long userNotExistId = 5L;

        List<ItemDto> items = service.searchItems("%Description%", userId);

        assertEquals(2, items.size());

        assertThrows(NotFoundException.class, () -> {
            service.searchItems("%Description%", userNotExistId);
        });

    }

    @Test
    @Order(6)
    void createComment() {
        long userId = 2L;
        long userNotExistId = 5L;
        long itemId = 3L;
        long itemNotFoundId = 5L;

        List<CommentDto> comments = service.getAllComments();

        assertEquals(getComments().size(), comments.size());

        CommentCreateDto newCreateComment = new CommentCreateDto("comment1");
        CommentDto newComment = service.createComment(itemId, userId, newCreateComment);

        assertEquals(getComments().size() + 1, service.getAllComments().size());

        assertThrows(NotFoundException.class, () -> {
            service.createComment(itemId, userNotExistId, newCreateComment);
        });

        assertThrows(NotFoundException.class, () -> {
            service.createComment(itemNotFoundId, userId, newCreateComment);
        });

    }
}
