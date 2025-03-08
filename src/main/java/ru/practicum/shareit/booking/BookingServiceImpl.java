package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.StateStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto getBooking(Long bookingId, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }
        return Optional.of(bookingRepository.getById(bookingId))
                .map(BookingMapper::toBookingDto)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено с ID: " + bookingId));
    }

    @Override
    public List<BookingDto> getAllBookings(Long userId, StateStatus state) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }
        Set<String> statusList = Set.of();
        if (state == StateStatus.ALL) {
            statusList = Set.of("WAITING", "APPROVED", "REJECTED", "CANCELED");
        }
        List<Booking> bookings = bookingRepository.findByBookerIdAndState(userId, statusList);
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public List<BookingDto> getAllOwnerBookings(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }
        List<Item> items = itemRepository.findByOwner_Id(userId);
        List<Long> itemsIds = items.stream()
                .map(Item::getId)
                .toList();
        List<Booking> bookings = bookingRepository.findAllByItem_Id(itemsIds);
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public BookingDto createItem(Long userId, BookingCreateDto bookingDto) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }
        Long itemId = bookingDto.getItemId();
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new NotFoundException("Вещь не найдена с ID: " + userId);
        }
        if (!item.get().getAvailable()) {
            throw new ValidationException("Вещь не доступна");
        }
        Booking booking = BookingMapper.toCreateBooking(user.get(), item.get(), bookingDto);
        booking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto updateStatus(Long bookingId, Boolean approve, Long userId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new NotFoundException("Бронирование не найдено с ID: " + bookingId);
        }
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ValidationException("Пользователь не найден с ID: " + userId);
        }
        Optional<Item> item = itemRepository.findById(booking.get().getItem().getId());
        if (item.isEmpty()) {
            throw new NotFoundException("Вещь не найдена");
        }
        if (approve) {
            booking.get().setStatus(BookingStatus.APPROVED);
        } else {
            booking.get().setStatus(BookingStatus.REJECTED);
        }

        bookingRepository.save(booking.get());
        return BookingMapper.toBookingDto(booking.get());
    }
}
