package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private final TestData testData = new TestData();

    private final ObjectMapper mapper = new ObjectMapper();

    private final UserMapper userMapper = new UserMapper();

    private UserDto userDto1;
    private UserDto userDto2;
    private UserDto userDto3;
    private UserCreateDto newUserDto;
    private UserDto createdUserDto;
    private UserUpdateDto updateUserDto;
    private UserDto updatedUserDto;


    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();

        userDto1 = userMapper.toUserDto(testData.user1);
        userDto2 = userMapper.toUserDto(testData.user2);
        userDto3 = userMapper.toUserDto(testData.user3);
    }

    @Test
    @Order(1)
    void GetUser() throws Exception {
        long userId = 2L;

        String jsonUser1 = new ObjectMapper().writeValueAsString(userDto1);

        when(userService.getUser(userId)).thenReturn(userDto1);

        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonUser1));

        verify(userService, times(1)).getUser(userId);
    }

    @Test
    @Order(2)
    void saveNewUser() throws Exception {
        newUserDto = new UserCreateDto("Username1", "user1@shareit.ru");
        createdUserDto = new UserDto(1L, "Username1", "user1@shareit.ru");

        when(userService.createUser(any())).thenReturn(createdUserDto);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(newUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(createdUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(createdUserDto.getName())))
                .andExpect(jsonPath("$.email", is(createdUserDto.getEmail())));

        verify(userService, times(1)).createUser(newUserDto);
    }

    @Test
    @Order(3)
    void deleteUser() throws Exception {
        long userId = userDto3.getId();

        when(userService.getUser(userId)).thenReturn(userDto3);
        doNothing().when(userService).removeUser(userId);

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).removeUser(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @Order(4)
    void updateUser() throws Exception {
        updateUserDto = new UserUpdateDto(userDto2.getId(), "Username2Updated", "user2_updated@shareit.ru");
        updatedUserDto = new UserDto(userDto2.getId(), "Username2Updated", "user2_updated@shareit.ru");

        when(userService.updateUser(updateUserDto)).thenReturn(updatedUserDto);

        mockMvc.perform(patch("/users/{id}", userDto2.getId())
                        .content(mapper.writeValueAsString(updateUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(updatedUserDto.getName())))
                .andExpect(jsonPath("$.email", is(updatedUserDto.getEmail())));

        verify(userService, times(1)).updateUser(updateUserDto);
    }

}
