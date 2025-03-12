package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    ItemBookingDto getItem(Long itemId);

    List<ItemDto> getItemsByUser(Long userId);

    ItemDto createItem(Long userId, ItemCreateDto itemDto);

    ItemDto updateItem(Long userId, ItemUpdateDto itemDto);

    List<ItemDto> searchItems(String text, Long userId);

    CommentDto createComment(Long itemId, Long userId, CommentCreateDto commentDto);

}
