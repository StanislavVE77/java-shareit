package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ItemRepositoryTest {

    private final TestData testData = new TestData();
    User user = testData.user1;
    Item item1 = testData.item1;
    Item item2 = testData.item2;
    Item item3 = testData.item3;

    List<Item> getItems() {
        return List.of(item1, item2, item3);
    }


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    ItemRepository itemRepository;


    @Test
    void findByOwnerId() {
        List<Item> itemsByOwnerId = itemRepository.findByOwner_Id(item1.getOwner().getId());

        assertEquals(itemsByOwnerId.size(), getItems().size(), "Число предметов по ID владельца (owner_id=1) не совпадают");
    }

    @Test
    void findByRequestId() {
        List<Item> itemsByRequestId = itemRepository.findByRequestId(item1.getRequest_id());

        assertEquals(itemsByRequestId.size(), 1, "Число предметов по ID запроса (request_id=1) должно быть равным 1");
    }

    @Test
    void searchItems() {
        List<Item> itemsSearch = itemRepository.searchItems("%Description%");

        assertEquals(2, itemsSearch.size(), "Число предметов, найденных по тексту описания  должно быть равным 1");
    }
}

