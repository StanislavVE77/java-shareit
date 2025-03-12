package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public ItemBookingDto toItemBookingDto(Item item, List<Booking> bookings, List<CommentDto> comments) {
        Booking lastBooking = getLastBooking(bookings);
        Booking nextBooking = getNextBooking(bookings);

        return new ItemBookingDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                comments,
                (lastBooking != null) ? new BookingShortDto(lastBooking.getId(), lastBooking.getStart(), lastBooking.getEnd()) : null,
                (nextBooking != null) ? new BookingShortDto(nextBooking.getId(), nextBooking.getStart(), nextBooking.getEnd()) : null
        );
    }

    private Booking getNextBooking(List<Booking> bookings) {
        return bookings.stream()
                .filter((Booking booking) -> booking.getStart().isAfter(LocalDateTime.now())).min(Comparator.comparing(Booking::getStart))
                .orElse(null);

    }

    private Booking getLastBooking(List<Booking> bookings) {
        return bookings.stream()
                .filter((Booking booking) -> booking.getStatus() == BookingStatus.CANCELED)
                .filter((Booking booking) -> booking.getEnd().isBefore(LocalDateTime.now())).max(Comparator.comparing(Booking::getEnd))
                .orElse(null);
    }

    public Item toCreateItem(User curUser, ItemCreateDto itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());

        User user = new User();
        user.setId(curUser.getId());
        user.setName(curUser.getName());
        user.setEmail(curUser.getEmail());
        item.setOwner(user);

        return item;
    }

    public Item toUpdateItem(User curUser, ItemUpdateDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());

        User user = new User();
        user.setId(curUser.getId());
        user.setName(curUser.getName());
        user.setEmail(curUser.getEmail());
        item.setOwner(user);

        return item;
    }

}

