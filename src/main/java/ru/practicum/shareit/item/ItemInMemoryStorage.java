package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ItemInMemoryStorage implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
    protected long seq = 0L;

    @Override
    public List<Item> getAllItemsByUserId(Long userId) {
        List<Item> items = getAllItems()
                .stream()
                .filter(item -> Objects.equals(item.getOwner().getId(), userId))
                .toList();
        return items;
    }

    @Override
    public Optional<Item> getItemById(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public Item createItem(Item item) {
        item.setId(generateId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Long itemId, Item item) {
        Item updItem = items.get(itemId);
        if (item.getName() != null && !item.getName().isEmpty()) {
            updItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isEmpty()) {
            updItem.setDescription(item.getDescription());
        }
        updItem.setAvailable(item.getAvailable());
        return updItem;
    }

    @Override
    public List<Item> searchItems(String text) {
        if (!text.isEmpty()) {
            List<Item> items = getAllItems()
                    .stream()
                    .filter(item -> (item.getName().toUpperCase().contains(text.toUpperCase())
                            || item.getDescription().toUpperCase().contains(text.toUpperCase()))
                            && item.getAvailable() == true)
                    .toList();
            return items;
        } else {
            return List.of();
        }
    }

    private long generateId() {
        return ++seq;
    }

    private Collection<Item> getAllItems() {
        return items.values();
    }

}
