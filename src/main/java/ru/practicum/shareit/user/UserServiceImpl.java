package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserInMemoryStorage userStorage;

    @Override
    public UserDto getUser(Long userId) {
        return userStorage.getUserById(userId)
                .map(UserMapper::toUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            throw new ValidationException("Имейл должен быть указан");
        }
        if (userDto.getName() == null || userDto.getName().isEmpty()) {
            throw new ValidationException("Логин должен быть указан");
        }
        Optional<User> alreadyExistUser = userStorage.getUserByEmail(userDto.getEmail());
        if (alreadyExistUser.isPresent()) {
            throw new ConflictException("Данный имейл уже используется");
        }
        User user = UserMapper.toUser(userDto);
        user = userStorage.createUser(user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) throws NotFoundException {
        UserDto updUserDto = getUser(userId);
        Optional<User> alreadyExistUser = userStorage.getUserByEmail(userDto.getEmail());
        if (alreadyExistUser.isPresent()) {
            throw new ConflictException("Данный имейл уже используется");
        }
        User user = UserMapper.toUser(userDto);
        user = userStorage.updateUser(userId, user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public void removeUser(Long userId) throws NotFoundException {
        UserDto delUserDto = getUser(userId);
        userStorage.removeUser(userId);
    }
}
