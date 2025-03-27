package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient requestClient;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PathVariable("id") Long requestId) {
        log.info("Gateway запрос GET /requests/{} c userId={}", requestId, userId);
        ResponseEntity<Object> request = requestClient.getItemRequest(userId, requestId);
        log.info("Gateway ответ GET /requests/{} вернул {}", requestId, request);
        return request;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @RequestBody @Valid ItemRequestCreateDto itemRequestDto) {
        log.info("Gateway запрос POST /requests с телом {}", itemRequestDto);
        ResponseEntity<Object> newRequest = requestClient.createItemRequest(userId, itemRequestDto);
        log.info("Gateway ответ POST /requests вернул {}", newRequest);
        return newRequest;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUserItemRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Gateway запрос GET /requests c userId={}", userId);
        ResponseEntity<Object> requests = requestClient.getUserItemRequests(userId);
        log.info("Gateway ответ GET /requests вернул {}", requests);
        return requests;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getOtherUserItemRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Gateway запрос GET /requests/all c userId={}", userId);
        ResponseEntity<Object> requests = requestClient.getOtherUsersItemRequests(userId);
        log.info("Gateway ответ GET /requests/all вернул {}", requests);
        return requests;
    }

}
