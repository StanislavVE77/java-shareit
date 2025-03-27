package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.StateStatus;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestBody BookingCreateDto bookingDto) {
        log.info("Server запрос POST /bookings с телом {}", bookingDto);
        BookingDto newBooking = bookingService.createBooking(userId, bookingDto);
        log.info("Server ответ POST /bookings вернул {}", newBooking);
        return newBooking;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable("id") Long bookingId) {
        log.info("Server запрос GET /bookings/{}  с userId={}", bookingId, userId);
        BookingDto booking = bookingService.getBooking(bookingId, userId);
        log.info("Server ответ GET /bookings/{} вернул {}", bookingId, booking);
        return booking;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto updateStatus(@PathVariable("id") Long bookingId,
                                   @RequestHeader("X-Sharer-User-Id") long userId,
                                   @RequestParam(value = "approved") Boolean approved) {
        log.info("Server запрос PATCH /bookings/{}?approved={} ", bookingId, approved);
        BookingDto booking = bookingService.updateStatus(bookingId, approved, userId);
        log.info("Server ответ PATCH /bookings/{}?approved={} вернул {}", bookingId, approved, booking);
        return booking;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAllBookings(@RequestParam(required = false, defaultValue = "ALL") StateStatus state,
                                           @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Server запрос GET /bookings  с userId={}", userId);
        List<BookingDto> bookings = bookingService.getAllBookings(userId, state);
        log.info("Server ответ GET /bookings вернул {}", bookings);
        return bookings;
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAllOwnerBookings(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Server запрос GET /bookings/owner  с userId={}", userId);
        List<BookingDto> bookings = bookingService.getAllOwnerBookings(userId);
        log.info("Server ответ GET /bookings/owner вернул {}", bookings);
        return bookings;
    }
}
