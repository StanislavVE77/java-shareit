package ru.practicum.shareit;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;


@Component
public class TestData {

    public User user1 = new User(2L, "Username2", "user2@shareit.ru");
    public User user2 = new User(3L, "Username3", "user3@shareit.ru");
    public User user3 = new User(4L, "Username4", "user4@shareit.ru");
    public Item newItem = new Item(1L, "Item1", "Description1", false, user1, 2L);
    public Item updateItem = new Item(2L, "Item2Updated", "Description2 Updated", false, user1, 2L);
    public Item item1 = new Item(2L, "Item2", "Description2", true, user1, 2L);
    public Item item2 = new Item(3L, "Item3", "Описание", false, user1, null);
    public Item item3 = new Item(4L, "Item4", "Description4", true, user1, null);
    public Comment newComment = new Comment(1L, "comment1", item2, user1, Instant.parse("2025-03-09T00:00:01.00Z"));
    public Comment comment1 = new Comment(2L, "comment2", item1, user2, Instant.parse("2025-03-10T00:00:01.00Z"));
    public Comment comment2 = new Comment(3L, "comment3", item2, user2, Instant.parse("2025-03-11T00:00:01.00Z"));
    public Comment comment3 = new Comment(4L, "comment4", item2, user1, Instant.parse("2025-03-12T00:00:01.00Z"));
    public Booking newBooking = new Booking(1L, LocalDateTime.of(2025, 3, 5, 0, 0, 1), LocalDateTime.of(2025, 4, 5, 0, 0, 1), item1, user1, BookingStatus.REJECTED);
    public Booking updateBooking = new Booking(2L, LocalDateTime.of(2025, 3, 28, 0, 0, 1), LocalDateTime.of(2025, 4, 1, 0, 0, 1), item1, user1, BookingStatus.WAITING);
    public Booking booking1 = new Booking(2L, LocalDateTime.of(2025, 3, 28, 0, 0, 1), LocalDateTime.of(2025, 4, 1, 0, 0, 1), item1, user1, BookingStatus.WAITING);
    public Booking booking2 = new Booking(3L, LocalDateTime.of(2025, 1, 1, 0, 0, 1), LocalDateTime.of(2025, 2, 1, 0, 0, 1), item1, user2, BookingStatus.CANCELED);
    public Booking booking3 = new Booking(4L, LocalDateTime.of(2025, 3, 5, 0, 0, 1), LocalDateTime.of(2025, 3, 12, 0, 0, 1), item2, user1, BookingStatus.APPROVED);
    public ItemRequest newRequest = new ItemRequest(1L, "Request description 1", Instant.parse("2025-03-01T18:22:23.00Z"), user1);
    public ItemRequest request1 = new ItemRequest(2L, "Request description 2", Instant.parse("2025-03-01T18:22:23.00Z"), user2);
    public ItemRequest request2 = new ItemRequest(3L, "Request description 3", Instant.parse("2025-03-03T00:00:00.00Z"), user3);
    public ItemRequest request3 = new ItemRequest(4L, "Request description 4", Instant.parse("2025-03-04T00:00:00.00Z"), user1);


}
