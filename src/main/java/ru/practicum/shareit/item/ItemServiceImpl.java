package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemMapper mapper;
    private final CommentMapper commentMapper;


    @Override
    public ItemBookingDto getItem(Long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        List<Comment> comments = commentRepository.findByItem_Id(itemId);
        List<CommentDto> commentsDto = commentMapper.toCommentsDto(comments);
        ItemBookingDto itemBookingDto = Optional.of(mapper.toItemBookingDto(item.get(), bookingRepository.findByItem_Id(itemId), commentsDto))
                .orElseThrow(() -> new NotFoundException("Вещь не найдена с ID: " + itemId));

        return itemBookingDto;
    }

    @Override
    public List<ItemDto> getItemsByUser(Long userId) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }

        return itemRepository.findByOwner_Id(userId)
                .stream()
                .map(mapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto createItem(Long userId, ItemCreateDto itemDto) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }
        Item item = mapper.toCreateItem(user.get(), itemDto);
        item = itemRepository.save(item);
        return mapper.toItemDto(item);
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long userId, ItemUpdateDto itemDto) {


        Optional<Item> item = itemRepository.findById(itemDto.getId());
        if (item.isEmpty()) {
            throw new NotFoundException("Вещь не найдена с ID: " + userId);
        }
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }
        if (itemDto.getName() != null) {
            item.get().setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.get().setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.get().setAvailable(itemDto.getAvailable());
        }
        item.get().setOwner(user.get());

        itemRepository.save(item.get());
        return mapper.toItemDto(item.get());
    }

    @Override
    public List<ItemDto> searchItems(String text, Long userId) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }

        List<Item> items = itemRepository.searchItems(text);
        return items.stream()
                .map(mapper::toItemDto)
                .toList();
    }

    @Override
    public CommentDto createComment(Long itemId, Long userId, CommentCreateDto commentDto) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new NotFoundException("Вещь не найдена с ID: " + userId);
        }
        Integer count = bookingRepository.getCountOfBooking(itemId, userId);
        if (count == 0) {
            throw new ValidationException("Пользователь не может оставлять комментария для данной вещи.");
        }
        Comment comment = commentMapper.toCreateComment(item.get(), user.get(), commentDto);
        comment = commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }
}
