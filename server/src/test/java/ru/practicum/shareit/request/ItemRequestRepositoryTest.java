package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ItemRequestRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    ItemRequestRepository requestRepository;

    @Test
    void findByRequestorIdOrderByCreatedDesc() {
        List<ItemRequest> itemsRequests = requestRepository.findByRequestor_IdOrderByCreatedDesc(4L);

        assertEquals(itemsRequests.size(), 2, "Число запросов по ID владельца (requestor_id=4) должно быть равным 2");
        assertEquals(itemsRequests.getFirst().getId(), 4, "Первым в списке должен быть запрос с requestor_id = 4");
    }

    @Test
    void findByRequestorIdNotOrderByCreatedDesc() {
        List<ItemRequest> itemsRequests = requestRepository.findByRequestor_IdNotOrderByCreatedDesc(4L);

        assertEquals(itemsRequests.size(), 1, "Число запросов созданных другими пользователями (requestor_id=4) должно быть равным 1");
    }
}
