package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    CommentRepository commentRepository;


    @Test
    void findByItemId() {
        List<Comment> commentsByItemId = commentRepository.findByItem_Id(3L);

        assertEquals(2, commentsByItemId.size(), "Число комментариев по предмету (item_id=3) не совпадают");
    }
}
