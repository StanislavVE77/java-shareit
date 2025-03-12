package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

public interface UserService {
    UserDto getUser(Long userId);

    UserDto createUser(UserCreateDto userDto);

    UserDto updateUser(UserUpdateDto userDto);

    void removeUser(Long userId);

}
