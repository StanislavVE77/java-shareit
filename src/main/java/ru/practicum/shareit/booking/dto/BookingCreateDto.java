package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreateDto {

    @FutureOrPresent
    private LocalDateTime start;

    @Future
    private LocalDateTime end;

    private Long itemId;

    private BookingStatus status = BookingStatus.WAITING;

    @AssertTrue
    boolean isStartBeforeEnd() {
        return start.isBefore(end);
    }
}
