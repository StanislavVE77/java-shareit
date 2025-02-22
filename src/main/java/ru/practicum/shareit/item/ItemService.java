package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {
    ItemDto getItem(Long itemId);

    List<ItemDto> getItemsByUser(Long userId);

    ItemDto createItem(Long userId, ItemCreateDto itemDto);

    ItemDto updateItem(Long userId, ItemUpdateDto itemDto);

    List<ItemDto> searchItems(String text, Long userId);

}
