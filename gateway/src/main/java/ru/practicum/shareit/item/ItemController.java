package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemsByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Gateway запрос GET /items");
        ResponseEntity<Object> items = itemClient.getItemsByUser(userId);
        log.info("Gateway ответ GET /items вернул {}", items);
        return items;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItem(@PathVariable("id") Long itemId) {
        log.info("Gateway запрос GET /items/{}", itemId);
        ResponseEntity<Object> item = itemClient.getItem(itemId);
        log.info("Gateway ответ GET /items/{} вернул {}", itemId, item);
        return item;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody @Valid ItemCreateDto itemDto) {
        log.info("Gateway запрос POST /items с телом {}", itemDto);
        ResponseEntity<Object> newItem = itemClient.createItem(userId, itemDto);
        log.info("Gateway ответ POST /items вернул {}", newItem);
        return newItem;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateItem(@PathVariable("id") Long itemId,
                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody ItemUpdateDto itemDto) {
        log.info("Gateway запрос PATCH /items с телом {}", itemDto);
        ResponseEntity<Object> newItem = itemClient.updateItem(itemId, userId, itemDto);
        log.info("Gateway ответ PATCH /items вернул {}", newItem);
        return newItem;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getSearchItems(@RequestParam(value = "text") String text,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Gateway запрос GET /items/search с text = {}", text);
        ResponseEntity<Object> items = itemClient.searchItems(text, userId);
        log.info("Gateway ответ GET /items/search вернул {}", items);
        return items;
    }

    @PostMapping("/{id}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createComment(@PathVariable("id") Long itemId,
                                                @RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestBody @Valid CommentCreateDto commentDto) {
        log.info("Gateway запрос POST /items/{}/comment с телом {}", itemId, commentDto);
        ResponseEntity<Object> newComment = itemClient.createComment(itemId, userId, commentDto);
        log.info("Gateway ответ POST /items/{id}/comment вернул {}", newComment);
        return newComment;
    }

}
