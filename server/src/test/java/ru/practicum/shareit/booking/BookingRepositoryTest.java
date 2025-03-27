package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    BookingRepository bookingRepository;

    @Test
    void findByBookerIdAndState() {
        Set<String> statusList = Set.of("WAITING");
        List<Booking> bookings = bookingRepository.findByBookerIdAndState(2L, statusList);

        assertEquals(bookings.size(), 1, "Число бронирований по ID пользователя (booker_id=2) со статусом 'WAITING' должно быть равным 1");
    }

    @Test
    void findByItemId() {
        List<Booking> bookings = bookingRepository.findByItem_Id(2L);

        assertEquals(bookings.size(), 2, "Число бронирований по ID вещи (item_id=2) должно быть равным 2");
    }

    @Test
    void findAllByItem_Id() {
        List<Long> itemsIds = List.of(2L, 3L);
        List<Booking> bookings = bookingRepository.findAllByItem_Id(itemsIds);

        assertEquals(bookings.size(), 3, "Число бронирований по списку IDs вещей (item_id: 2, 3) должно быть равным 3");
    }

    @Test
    void getCountOfBooking() {
        Integer count = bookingRepository.getCountOfBooking(3L, 2L);

        assertEquals(count, 1, "Число бронирований вещи (item_id = 3) пользователя (booker_id = 2) должно быть равным 1");
    }
}
