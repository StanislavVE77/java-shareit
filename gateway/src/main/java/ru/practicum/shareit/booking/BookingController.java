package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.StateStatus;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestBody @Valid BookingCreateDto bookingDto) {
        log.info("Gateway запрос POST /bookings с телом {}", bookingDto);
        ResponseEntity<Object> newBooking = bookingClient.createBooking(userId, bookingDto);
        log.info("Gateway ответ POST /bookings вернул {}", newBooking);
        return newBooking;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable("id") Long bookingId) {
        log.info("Gateway запрос GET /bookings/{}  с userId={}", bookingId, userId);
        ResponseEntity<Object> booking = bookingClient.getBooking(userId, bookingId);
        log.info("Gateway ответ GET /bookings/{} вернул {}", bookingId, booking);
        return booking;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateStatus(@PathVariable("id") Long bookingId,
                                               @RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam(value = "approved") Boolean approved) {
        log.info("Gateway запрос PATCH /bookings/{}?approved={} ", bookingId, approved);
        ResponseEntity<Object> booking = bookingClient.updateStatus(bookingId, approved, userId);
        log.info("Gateway ответ PATCH /bookings/{}?approved={} вернул {}", bookingId, approved, booking);
        return booking;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllBookings(@RequestParam(required = false, defaultValue = "ALL") StateStatus state,
                                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Gateway запрос GET /bookings  с userId={}", userId);
        ResponseEntity<Object> bookings = bookingClient.getBookings(userId, state);
        log.info("Gateway ответ GET /bookings вернул {}", bookings);
        return bookings;
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllOwnerBookings(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Gateway запрос GET /bookings/owner  с userId={}", userId);
        ResponseEntity<Object> bookings = bookingClient.getAllOwnerBookings(userId);
        log.info("Gateway ответ GET /bookings/owner вернул {}", bookings);
        return bookings;
    }
}
