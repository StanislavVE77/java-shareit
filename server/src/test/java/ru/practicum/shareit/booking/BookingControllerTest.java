package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.StateStatus;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {
    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    public ObjectMapper objectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return objectMapper;
    }

    private final TestData testData = new TestData();

    private final BookingMapper bookingMapper = new BookingMapper();

    BookingDto bookingDto1;
    BookingDto bookingDto2;
    BookingDto bookingDto3;
    BookingDto createdBookingDto;
    BookingDto updatedBookingDto;

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();

        bookingDto1 = bookingMapper.toBookingDto(testData.booking1);
        bookingDto2 = bookingMapper.toBookingDto(testData.booking2);
        bookingDto3 = bookingMapper.toBookingDto(testData.booking3);
        createdBookingDto = bookingMapper.toBookingDto(testData.newBooking);
        updatedBookingDto = bookingMapper.toBookingDto(testData.updateBooking);

    }

    @Test
    @Order(1)
    void GetBookings() throws Exception {
        String state = "ALL";
        long userId = 2L;

        List<BookingDto> bookings = List.of(bookingDto1, bookingDto3);
        final String jsonBookings = "[{" +
                "\"id\": 2," +
                "\"start\":\"2025-03-28T00:00:01\"," +
                "\"end\":\"2025-04-01T00:00:01\"," +
                "\"booker\":{" +
                "\"id\": 2," +
                "\"name\":\"Username2\"," +
                "\"email\":\"user2@shareit.ru\"" +
                "}," +
                "\"item\":{" +
                "\"id\": 2," +
                "\"name\":\"Item2\"," +
                "\"description\":\"Description2\"," +
                "\"available\":true," +
                "\"owner\":{" +
                "\"id\": 2," +
                "\"name\":\"Username2\"," +
                "\"email\":\"user2@shareit.ru\"" +
                "}," +
                "\"request_id\":2" +
                "}," +
                "\"status\":\"WAITING\"" +
                "},{ " +
                "\"id\": 4," +
                "\"start\":\"2025-03-05T00:00:01\"," +
                "\"end\":\"2025-03-12T00:00:01\"," +
                "\"booker\":{" +
                "\"id\": 2," +
                "\"name\":\"Username2\"," +
                "\"email\":\"user2@shareit.ru\"" +
                "}," +
                "\"item\":{" +
                "\"id\": 3," +
                "\"name\":\"Item3\"," +
                "\"description\":\"Описание\"," +
                "\"available\":false," +
                "\"owner\":{" +
                "\"id\": 2," +
                "\"name\":\"Username2\"," +
                "\"email\":\"user2@shareit.ru\"" +
                "}," +
                "\"request_id\":null" +
                "}," +
                "\"status\":\"APPROVED\"" +
                "}]";

        when(bookingService.getAllBookings(userId, StateStatus.ALL)).thenReturn(bookings);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonBookings));

        verify(bookingService, times(1)).getAllBookings(userId, StateStatus.ALL);
    }

    @Test
    @Order(2)
    void GetBooking() throws Exception {
        long userId = 2L;

        String jsonBooking1 = objectMapper().writeValueAsString(bookingDto1);

        when(bookingService.getBooking(bookingDto1.getId(), userId)).thenReturn(bookingDto1);

        mockMvc.perform(get("/bookings/" + bookingDto1.getId())
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonBooking1));

        verify(bookingService, times(1)).getBooking(bookingDto1.getId(), userId);
    }

    @Test
    @Order(3)
    void getAllOwnerBookings() throws Exception {
        long userId = 2L;

        List<BookingDto> bookings = List.of(bookingDto1, bookingDto2, bookingDto3);

        String jsonBookings = objectMapper().writeValueAsString(bookings);

        when(bookingService.getAllOwnerBookings(userId)).thenReturn(bookings);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonBookings));

        verify(bookingService, times(1)).getAllOwnerBookings(userId);
    }

    @Test
    @Order(4)
    void createBooking() throws Exception {
        long userId = 2L;

        BookingCreateDto newBookingDto = new BookingCreateDto(LocalDateTime.of(2025, 3, 5, 0, 0, 1),
                LocalDateTime.of(2025, 4, 5, 0, 0, 1), 2L, BookingStatus.WAITING);
        String jsonNewBooking = objectMapper().writeValueAsString(createdBookingDto);

        when(bookingService.createBooking(userId, newBookingDto)).thenReturn(createdBookingDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper().writeValueAsString(newBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonNewBooking));

        verify(bookingService, times(1)).createBooking(userId, newBookingDto);

    }

    @Test
    @Order(5)
    void updateStatus() throws Exception {
        long userId = 2L;
        long bookingId = 2L;
        String approved = "false";

        String jsonUpdateBooking = objectMapper().writeValueAsString(updatedBookingDto);

        when(bookingService.updateStatus(bookingId, false, userId)).thenReturn(updatedBookingDto);

        mockMvc.perform(patch("/bookings/" + bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", approved))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonUpdateBooking));

        verify(bookingService, times(1)).updateStatus(bookingId, false, userId);
    }

}
