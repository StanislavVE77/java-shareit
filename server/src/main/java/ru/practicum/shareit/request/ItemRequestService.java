package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestWithItemsDto getItemRequest(Long userId, Long requestId);

    List<ItemRequestWithItemsDto> getUserItemRequests(Long userId);

    List<ItemRequestDto> getOtherUsersItemRequests(Long userId);

    ItemRequestDto createItemRequest(Long userId, ItemRequestCreateDto itemRequestDto);

    List<ItemRequestDto> getAllItemRequests();

}
