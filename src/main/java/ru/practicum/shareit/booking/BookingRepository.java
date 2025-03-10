package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Set;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "select * from bookings where booker_id = ?1 and status in ( ?2 ) ", nativeQuery = true)
    List<Booking> findByBookerIdAndState(Long userId, Set<String> state);

    List<Booking> findByItem_Id(Long itemId);

    @Query("select booking from Booking booking " +
            "where booking.item.getId() in ( ?1 ) ")
    List<Booking> findAllByItem_Id(Iterable<Long> ids);

    @Query(value = "select count(id) as count from bookings " +
            "where item_id = ?1 and booker_id = ?2 and status = 'APPROVED' " +
            "and end_date_time < CURRENT_TIMESTAMP", nativeQuery = true)
    Integer getCountOfBooking(Long itemId, Long userId);
}

