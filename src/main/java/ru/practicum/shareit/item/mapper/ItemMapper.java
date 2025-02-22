package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static Item toCreateItem(User curUser, ItemCreateDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());

        User user = new User();
        user.setId(curUser.getId());
        user.setName(curUser.getName());
        user.setEmail(curUser.getEmail());
        item.setOwner(user);

        return item;
    }

    public static Item toUpdateItem(User curUser, ItemUpdateDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());

        User user = new User();
        user.setId(curUser.getId());
        user.setName(curUser.getName());
        user.setEmail(curUser.getEmail());
        item.setOwner(user);

        return item;
    }

}

