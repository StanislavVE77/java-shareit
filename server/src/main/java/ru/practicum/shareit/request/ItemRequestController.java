package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService requestService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestWithItemsDto getItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @PathVariable("id") Long requestId) {
        log.info("Server запрос GET /requests/{} c userId={}", requestId, userId);
        ItemRequestWithItemsDto request = requestService.getItemRequest(userId, requestId);
        log.info("Server ответ GET /requests/{} вернул {}", requestId, request);
        return request;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto createItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestBody ItemRequestCreateDto itemRequestDto) {
        log.info("Server запрос POST /requests с телом {}", itemRequestDto);
        ItemRequestDto newRequest = requestService.createItemRequest(userId, itemRequestDto);
        log.info("Server ответ POST /requests вернул {}", newRequest);
        return newRequest;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestWithItemsDto> getUserItemRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Server запрос GET /requests c userId={}", userId);
        List<ItemRequestWithItemsDto> requests = requestService.getUserItemRequests(userId);
        log.info("Server ответ GET /requests вернул {}", requests);
        return requests;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> getOtherUserItemRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Server запрос GET /requests/all c userId={}", userId);
        List<ItemRequestDto> requests = requestService.getOtherUsersItemRequests(userId);
        log.info("Server ответ GET /requests/all вернул {}", requests);
        return requests;
    }

}
