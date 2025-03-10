package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.StateStatus;

import java.util.List;


public interface BookingService {
    BookingDto getBooking(Long bookingId, Long userId);

    List<BookingDto> getAllBookings(Long userId, StateStatus state);

    List<BookingDto> getAllOwnerBookings(Long userId);

    BookingDto createItem(Long userId, BookingCreateDto bookingDto);

    BookingDto updateStatus(Long bookingId, Boolean approve, Long userId);
}