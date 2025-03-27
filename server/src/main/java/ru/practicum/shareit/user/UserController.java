package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable("id") Long userId) {
        log.info("Server запрос GET /users/{}", userId);
        UserDto user = userService.getUser(userId);
        log.info("Server ответ GET /users/{} вернул {}", userId, user);
        return user;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserCreateDto userDto) {
        log.info("Server запрос POST /users с телом {}", userDto);
        UserDto newUser = userService.createUser(userDto);
        log.info("Server ответ POST /users вернул {}", newUser);
        return newUser;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@PathVariable("id") Long userId,
                              @RequestBody UserUpdateDto userDto) {
        log.info("Server запрос PATCH /users с телом {} и id={}", userDto, userId);
        userDto.setId(userId);
        UserDto newUser = userService.updateUser(userDto);
        log.info("Server ответ PATCH /users вернул {}", newUser);
        return newUser;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable("userId") Long userId) {
        log.info("Server запрос DELETE /users/{}", userId);
        userService.removeUser(userId);
        log.info("Server ответ DELETE /users/{} успешно выполнен", userId);
    }

}
