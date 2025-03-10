package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    List<Item> getAllItemsByUserId(Long userId);

    Optional<Item> getItemById(Long itemId);

    Item createItem(Item item);

    Item updateItem(Item item);

    List<Item> searchItems(String text);

}
