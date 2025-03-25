package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    public BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getBooker(),
                booking.getItem(),
                booking.getStatus()
        );
    }

    public Booking toCreateBooking(User curUser, Item curItem, BookingCreateDto bookingDto) {
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setStatus(bookingDto.getStatus());

        User user = new User();
        user.setId(curUser.getId());
        user.setName(curUser.getName());
        user.setEmail(curUser.getEmail());
        booking.setBooker(user);

        Item item = new Item();
        item.setId(curItem.getId());
        item.setName(curItem.getName());
        item.setDescription(curItem.getDescription());
        item.setAvailable(curItem.getAvailable());
        booking.setItem(item);

        return booking;
    }
}
