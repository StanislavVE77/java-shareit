package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.StateStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

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
    private final TestData testData = new TestData();

    BookingDto bookingDto1 = mapper.toBookingDto(testData.booking1);
    BookingDto bookingDto2 = mapper.toBookingDto(testData.booking2);
    BookingDto bookingDto3 = mapper.toBookingDto(testData.booking3);

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
        assertFalse(testData.booking1.equals(testData.booking2));

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

