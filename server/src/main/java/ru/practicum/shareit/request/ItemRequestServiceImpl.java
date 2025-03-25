package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper mapper;

    @Override
    public ItemRequestWithItemsDto getItemRequest(Long userId, Long requestId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }
        List<Item> items = itemRepository.findByRequestId(requestId);

        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден с ID: " + requestId));
        return mapper.toItemRequestWithItemsDto(request, items);
    }

    @Override
    public List<ItemRequestWithItemsDto> getUserItemRequests(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }
        List<ItemRequestWithItemsDto> requestsWithItems = new ArrayList<>();
        List<ItemRequest> requests = requestRepository.findByRequestor_IdOrderByCreatedDesc(userId);
        for (ItemRequest request : requests) {
            List<Item> items = itemRepository.findByRequestId(request.getId());
            requestsWithItems.add(mapper.toItemRequestWithItemsDto(request, items));
        }
        return requestsWithItems;
    }

    @Override
    public List<ItemRequestDto> getOtherUsersItemRequests(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }
        List<ItemRequest> requests = requestRepository.findByRequestor_IdNotOrderByCreatedDesc(userId);
        return requests.stream()
                .map(mapper::toItemRequestDto)
                .toList();
    }

    @Override
    public ItemRequestDto createItemRequest(Long userId, ItemRequestCreateDto itemRequestDto) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }
        ItemRequest request = mapper.toCreateItemRequest(user.get(), itemRequestDto);
        request = requestRepository.save(request);
        return mapper.toItemRequestDto(request);
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests() {
        List<ItemRequest> itemRequests = requestRepository.findAll();
        return itemRequests.stream()
                .map(mapper::toItemRequestDto)
                .toList();
    }

}
