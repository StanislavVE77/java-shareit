package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UserRepositoryTest {
    static User user1 = new User(2L, "Username2", "user2@shareit.ru");

    List<User> getUsers() {
        return List.of(user1);
    }

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Test
    void findById() {

        Optional<User> userBuId = userRepository.findById(user1.getId());

        assertEquals(userBuId.get(), user1, "Данные не совпадают");
    }
}
