package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ItemBookingDto {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private List<CommentDto> comments;

    private BookingShortDto lastBooking;

    private BookingShortDto nextBooking;

}
