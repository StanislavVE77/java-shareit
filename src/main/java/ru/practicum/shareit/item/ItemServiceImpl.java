package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserInMemoryStorage;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemInMemoryStorage itemStorage;
    private final UserInMemoryStorage userStorage;

    @Override
    public ItemDto getItem(Long itemId) {
        return itemStorage.getItemById(itemId)
                .map(ItemMapper::toItemDto)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена с ID: " + itemId));
    }

    @Override
    public List<ItemDto> getItemsByUser(Long userId) {
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }
        return itemStorage.getAllItemsByUserId(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto createItem(Long userId, ItemCreateDto itemDto) {
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }
        Item item = ItemMapper.toCreateItem(user.get(), itemDto);
        item = itemStorage.createItem(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(Long userId, ItemUpdateDto itemDto) throws NotFoundException {
        getItem(itemDto.getId());
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }
        Item item = ItemMapper.toUpdateItem(user.get(), itemDto);
        if (!Objects.equals(userId, item.getOwner().getId())) {
            throw new NotFoundException("У вещи другой владелец");
        }
        item = itemStorage.updateItem(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> searchItems(String text, Long userId) {
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }
        return itemStorage.searchItems(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }
}
