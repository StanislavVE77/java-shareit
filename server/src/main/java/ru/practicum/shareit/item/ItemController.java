package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getItemsByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Server запрос GET /items");
        List<ItemDto> items = itemService.getItemsByUser(userId);
        log.info("Server ответ GET /items вернул {}", items);
        return items;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemBookingDto getItem(@PathVariable("id") Long itemId) {
        log.info("Server запрос GET /items/{}", itemId);
        ItemBookingDto item = itemService.getItem(itemId);
        log.info("Server ответ GET /items/{} вернул {}", itemId, item);
        return item;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @RequestBody ItemCreateDto itemDto) {
        log.info("Server запрос POST /items с телом {}", itemDto);
        ItemDto newItem = itemService.createItem(userId, itemDto);
        log.info("Server ответ POST /items вернул {}", newItem);
        return newItem;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@PathVariable("id") Long itemId,
                              @RequestHeader("X-Sharer-User-Id") long userId,
                              @RequestBody ItemUpdateDto itemDto) {
        log.info("Server запрос PATCH /items с телом {}", itemDto);
        itemDto.setId(itemId);
        ItemDto newItem = itemService.updateItem(userId, itemDto);
        log.info("Server ответ PATCH /items вернул {}", newItem);
        return newItem;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getSearchItems(@RequestParam(value = "text") String text,
                                        @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Server запрос GET /items/search с text = {}", text);
        List<ItemDto> items = itemService.searchItems(text, userId);
        log.info("Server ответ GET /items/search вернул {}", items);
        return items;
    }

    @PostMapping("/{id}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable("id") Long itemId,
                                    @RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestBody CommentCreateDto commentDto) {
        log.info("Server запрос POST /items/{}/comment с телом {}", itemId, commentDto);
        CommentDto newComment = itemService.createComment(itemId, userId, commentDto);
        log.info("Server ответ POST /items/{id}/comment вернул {}", newComment);
        return newComment;
    }

}
