package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    ItemRepository itemRepository;

    User user = new User(2L, "Username2", "user2@shareit.ru");
    Item item1 = new Item(2L, "Item2", "Description2", true, user, 2L);
    Item item2 = new Item(3L, "Item3", "Описание", false, user, null);
    Item item3 = new Item(4L, "Item4", "Description4", true, user, null);

    List<Item> items = List.of(item1, item2, item3);

    @Test
    void findByOwnerId() {
        List<Item> itemsByOwnerId = itemRepository.findByOwner_Id(item1.getOwner().getId());

        assertEquals(itemsByOwnerId.size(), items.size(), "Число предметов по ID владельца (owner_id=1) не совпадают");
    }

    @Test
    void findByRequestId() {
        List<Item> itemsByRequestId = itemRepository.findByRequestId(item1.getRequestId());

        assertEquals(itemsByRequestId.size(), 1, "Число предметов по ID запроса (request_id=1) должно быть равным 1");
    }

    @Test
    void searchItems() {
        List<Item> itemsSearch = itemRepository.searchItems("%Description%");

        assertEquals(2, itemsSearch.size(), "Число предметов, найденных по тексту описания  должно быть равным 1");
    }
}

