package ru.practicum.shareit.booking.dto;

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

    private LocalDateTime start;

    private LocalDateTime end;

    private Long itemId;

    private BookingStatus status = BookingStatus.WAITING;

    public boolean isStartBeforeEnd() {
        return start.isBefore(end);
    }
}
