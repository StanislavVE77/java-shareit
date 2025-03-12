package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getItemsByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Пришел GET запрос /items");
        List<ItemDto> items = itemService.getItemsByUser(userId);
        log.info("Метод GET /items вернул ответ {}", items);
        return items;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemBookingDto getItem(@PathVariable("id") Long itemId) {
        log.info("Пришел GET запрос /items/{}", itemId);
        ItemBookingDto item = itemService.getItem(itemId);
        log.info("Метод GET /items/{} вернул ответ {}", itemId, item);
        return item;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @RequestBody @Valid ItemCreateDto itemDto) {
        log.info("Пришел POST запрос /items с телом {}", itemDto);
        ItemDto newItem = itemService.createItem(userId, itemDto);
        log.info("Метод POST /items вернул ответ {}", newItem);
        return newItem;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@PathVariable("id") Long itemId,
                              @RequestHeader("X-Sharer-User-Id") long userId,
                              @RequestBody ItemUpdateDto itemDto) {
        log.info("Пришел PATCH запрос /items с телом {}", itemDto);
        itemDto.setId(itemId);
        ItemDto newItem = itemService.updateItem(userId, itemDto);
        log.info("Метод PATCH /items вернул ответ {}", newItem);
        return newItem;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getSearchItems(@RequestParam(value = "text") String text,
                                        @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Пришел GET запрос /items/search с text = {}", text);
        List<ItemDto> items = itemService.searchItems(text, userId);
        log.info("Метод GET /items/search вернул ответ {}", items);
        return items;
    }

    @PostMapping("/{id}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable("id") Long itemId,
                                    @RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestBody @Valid CommentCreateDto commentDto) {
        log.info("Пришел POST запрос /items/{}/comment с телом {}", itemId, commentDto);
        CommentDto newComment = itemService.createComment(itemId, userId, commentDto);
        log.info("Метод POST /items/{id}/comment вернул ответ {}", newComment);
        return newComment;
    }

}
