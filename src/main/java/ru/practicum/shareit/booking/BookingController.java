package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.StateStatus;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestBody @Valid BookingCreateDto bookingDto) {
        log.info("Пришел POST запрос /bookings с телом {}", bookingDto);
        BookingDto newBooking = bookingService.createItem(userId, bookingDto);
        log.info("Метод POST /bookings вернул ответ {}", newBooking);
        return newBooking;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable("id") Long bookingId) {
        log.info("Пришел GET запрос /bookings/{}  с userId={}", bookingId, userId);
        BookingDto booking = bookingService.getBooking(bookingId, userId);
        log.info("Метод GET /bookings/{} вернул ответ {}", bookingId, booking);
        return booking;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto updateStatus(@PathVariable("id") Long bookingId,
                                   @RequestHeader("X-Sharer-User-Id") long userId,
                                   @RequestParam(value = "approved") Boolean approve) {
        log.info("Пришел PATCH запрос /bookings/{}?approved={} ", bookingId, approve);
        BookingDto booking = bookingService.updateStatus(bookingId, approve, userId);
        log.info("Метод PATCH /bookings/{}?approved={} вернул ответ {}", bookingId, approve, booking);
        return booking;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAllBookings(@RequestParam(required = false, defaultValue = "ALL") StateStatus state,
                                           @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Пришел GET запрос /bookings  с userId={}", userId);
        List<BookingDto> bookings = bookingService.getAllBookings(userId, state);
        log.info("Метод GET /bookings вернул ответ {}", bookings);
        return bookings;
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAllOwnerBookings(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Пришел GET запрос /bookings/owner  с userId={}", userId);
        List<BookingDto> bookings = bookingService.getAllOwnerBookings(userId);
        log.info("Метод GET /bookings/owner вернул ответ {}", bookings);
        return bookings;
    }
}
