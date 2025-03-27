package ru.practicum.shareit.request.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ItemRequestMapper {

    public ItemRequestDto toItemRequestDto(ItemRequest request) {
        return new ItemRequestDto(
                request.getId(),
                request.getDescription(),
                request.getRequestor(),
                request.getCreated()
        );
    }

    public ItemRequestWithItemsDto toItemRequestWithItemsDto(ItemRequest request, List<Item> items) {
        return new ItemRequestWithItemsDto(
                request.getId(),
                request.getDescription(),
                request.getRequestor(),
                request.getCreated(),
                items
        );
    }

    public ItemRequest toCreateItemRequest(User curUser, ItemRequestCreateDto itemRequestDto) {
        ItemRequest request = new ItemRequest();
        request.setDescription(itemRequestDto.getDescription());

        User user = new User();
        user.setId(curUser.getId());
        user.setName(curUser.getName());
        user.setEmail(curUser.getEmail());
        request.setRequestor(user);

        return request;
    }

}
