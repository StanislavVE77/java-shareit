package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class UserServiceTest {
    private final EntityManager em;
    private final UserService service;
    private final UserMapper mapper = new UserMapper();

    User user1 = new User(2L, "Username2", "user2@shareit.ru");
    User user2 = new User(3L, "Username3", "user3@shareit.ru");
    User user3 = new User(4L, "Username4", "user4@shareit.ru");
    UserDto userDto1 = mapper.toUserDto(user1);
    UserDto userDto2 = mapper.toUserDto(user2);
    UserDto userDto3 = mapper.toUserDto(user3);

    List<UserDto> getUsers() {
        return List.of(userDto1, userDto2, userDto3);
    }

    @Test
    @Order(1)
    void getUser() {
        UserDto user = service.getUser(userDto1.getId());

        assertEquals(userDto1, user);
    }

    @Test
    @Order(2)
    void createAndRemoveUser() {
        List<UserDto> users = service.getAllUsers();

        assertEquals(getUsers().size(), users.size());

        UserCreateDto newCreateUserwoLogin = new UserCreateDto("", "user1@shareit.ru");
        assertThrows(ValidationException.class, () -> {
            service.createUser(newCreateUserwoLogin);
        });



        UserCreateDto newCreateUserwoEmail = new UserCreateDto("Username1", "");
        assertThrows(ValidationException.class, () -> {
            service.createUser(newCreateUserwoEmail);
        });


        UserCreateDto newCreateUser = new UserCreateDto("Username1", "user1@shareit.ru");
        UserDto newUser = service.createUser(newCreateUser);

        assertEquals(getUsers().size() + 1, service.getAllUsers().size());

        service.removeUser(newUser.getId());

        assertEquals(getUsers().size(), service.getAllUsers().size());
    }

    @Test
    @Order(3)
    void updateUser() {
        long userId = userDto1.getId();

        UserUpdateDto updateUser = new UserUpdateDto(userId, "Username1 Updated", "user1_updated@shareit.ru");
        UserDto user = service.updateUser(updateUser);

        user = service.getUser(userId);

        assertNotEquals(userDto1.getName(), user.getName());
        assertEquals(updateUser.getName(), user.getName());
        assertNotEquals(userDto1.getEmail(), user.getEmail());
        assertEquals(updateUser.getEmail(), user.getEmail());

        UserUpdateDto updateUser2 = new UserUpdateDto(userId, null, null);
        assertDoesNotThrow(() -> service.updateUser(updateUser2));

    }
}
