package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public UserDto getUser(Long userId) {
        User user = Optional.of(userRepository.getById(userId))
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));
        return mapper.toUserDto(user);
    }

    @Override
    public UserDto createUser(UserCreateDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            throw new ValidationException("Имейл должен быть указан");
        }
        if (userDto.getName() == null || userDto.getName().isEmpty()) {
            throw new ValidationException("Логин должен быть указан");
        }
        User user = mapper.toCreateUser(userDto);
        user = userRepository.save(user);
        return mapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(UserUpdateDto userDto) throws NotFoundException {
        UserDto updUserDto = getUser(userDto.getId());
        User user = mapper.toUpdateUser(userDto);
        if (userDto.getEmail() == null) {
            user.setEmail(updUserDto.getEmail());
        }
        if (userDto.getName() == null) {
            user.setName(updUserDto.getName());
        }
        user = userRepository.save(user);
        return mapper.toUserDto(user);
    }

    @Override
    public void removeUser(Long userId) throws NotFoundException {
        User user = mapper.toUser(getUser(userId));
        userRepository.delete(user);
    }


}
