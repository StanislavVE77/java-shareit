package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.StateStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class BookingServiceTest {
    private final EntityManager em;
    private final BookingService service;
    private final BookingMapper mapper = new BookingMapper();

    User user1 = new User(2L, "Username2", "user2@shareit.ru");
    User user2 = new User(3L, "Username3", "user3@shareit.ru");
    User user3 = new User(4L, "Username4", "user4@shareit.ru");
    Item item1 = new Item(2L, "Item2", "Description2", true, user1, 2L);
    Item item2 = new Item(3L, "Item3", "Описание", false, user1, null);
    Item item3 = new Item(4L, "Item4", "Description4", true, user1, null);
    Booking booking1 = new Booking(2L, LocalDateTime.of(2025, 3, 28, 0, 0, 1), LocalDateTime.of(2025, 4, 1, 0, 0, 1), item1, user1, BookingStatus.WAITING);
    Booking booking2 = new Booking(3L, LocalDateTime.of(2025, 1, 1, 0, 0, 1), LocalDateTime.of(2025, 2, 1, 0, 0, 1), item1, user2, BookingStatus.CANCELED);
    Booking booking3 = new Booking(4L, LocalDateTime.of(2025, 3, 5, 0, 0, 1), LocalDateTime.of(2025, 3, 12, 0, 0, 1), item2, user1, BookingStatus.APPROVED);
    BookingDto bookingDto1 = mapper.toBookingDto(booking1);
    BookingDto bookingDto2 = mapper.toBookingDto(booking2);
    BookingDto bookingDto3 = mapper.toBookingDto(booking3);

    List<BookingDto> getBookings() {
        return List.of(bookingDto1, bookingDto3);
    }

    @Test
    @Order(1)
    void getAllBookings() {
        long userId = 2L;
        StateStatus status = StateStatus.ALL;

        List<BookingDto> bookings = service.getAllBookings(userId, status);

        assertEquals(getBookings(), bookings);

        long userNotFoundId = 5L;
        assertThrows(NotFoundException.class, () -> {
            service.getAllBookings(userNotFoundId, status);
        });

    }

    @Test
    @Order(2)
    void getOneBooking() {
        long userId = 2L;
        long userNotFoundId = 5L;

        BookingDto booking = service.getBooking(2L, userId);

        assertEquals(bookingDto1, booking);
        assertTrue(bookingDto1.equals(booking));
        assertFalse(booking1.equals(booking2));

        assertThrows(NotFoundException.class, () -> {
            service.getBooking(2L, userNotFoundId);
        });

    }

    @Test
    @Order(3)
    void getAllOwnerBookings() {
        long userId = 2L;
        long userNotFoundId = 5L;

        List<BookingDto> bookings = service.getAllOwnerBookings(userId);

        assertEquals(bookings.size(), 3);

        assertThrows(NotFoundException.class, () -> {
            service.getAllOwnerBookings(userNotFoundId);
        });

    }

    @Test
    @Order(4)
    void createBooking() {
        long userId = 2L;

        List<BookingDto> allBookings1 = service.getAllBookings(userId, StateStatus.ALL);

        assertEquals(allBookings1.size(), 2);

        BookingCreateDto bookingToSave = new BookingCreateDto(LocalDateTime.of(2025, 3, 5, 0, 0, 1),
                LocalDateTime.of(2025, 4, 5, 0, 0, 1), 2L, BookingStatus.WAITING);
        BookingCreateDto bookingToSaveNoItem = new BookingCreateDto(LocalDateTime.of(2025, 3, 5, 0, 0, 1),
                LocalDateTime.of(2025, 4, 5, 0, 0, 1), 5L, BookingStatus.WAITING);
        BookingCreateDto bookingToSaveFailItem = new BookingCreateDto(LocalDateTime.of(2025, 3, 5, 0, 0, 1),
                LocalDateTime.of(2025, 4, 5, 0, 0, 1), 3L, BookingStatus.WAITING);

        BookingDto booking = service.createBooking(userId, bookingToSave);

        List<BookingDto> allBookings2 = service.getAllBookings(userId, StateStatus.ALL);

        assertEquals(allBookings2.size(), 3);

        assertThrows(NotFoundException.class, () -> {
            service.createBooking(5L, bookingToSave);
        });

        assertThrows(NotFoundException.class, () -> {
            service.createBooking(userId, bookingToSaveNoItem);
        });

        assertThrows(ValidationException.class, () -> {
            service.createBooking(userId, bookingToSaveFailItem);
        });

    }

    @Test
    @Order(5)
    void updateBookingStatus() {
        long userId = 2L;

        assertEquals(bookingDto1.getStatus(), BookingStatus.WAITING);

        BookingDto updatedBooking = service.updateStatus(bookingDto1.getId(), false, userId);

        assertEquals(updatedBooking.getStatus(), BookingStatus.REJECTED);

        updatedBooking = service.updateStatus(bookingDto1.getId(), true, userId);

        assertEquals(updatedBooking.getStatus(), BookingStatus.APPROVED);

        assertThrows(NotFoundException.class, () -> {
            service.updateStatus(5L, false, userId);
        });

        assertThrows(ValidationException.class, () -> {
            service.updateStatus(bookingDto1.getId(), false, 5L);
        });

    }
}

