package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable("id") Long userId) {
        log.info("Пришел GET запрос /users/{}", userId);
        UserDto user = userService.getUser(userId);
        log.info("Метод GET /users/{} вернул ответ {}", userId, user);
        return user;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid UserCreateDto userDto) {
        log.info("Пришел POST запрос /users с телом {}", userDto);
        UserDto newUser = userService.createUser(userDto);
        log.info("Метод POST /users вернул ответ {}", newUser);
        return newUser;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@PathVariable("id") Long userId,
                              @RequestBody UserUpdateDto userDto) {
        log.info("Пришел PATCH запрос /users с телом {} и id={}", userDto, userId);
        userDto.setId(userId);
        UserDto newUser = userService.updateUser(userDto);
        log.info("Метод PATCH /users вернул ответ {}", newUser);
        return newUser;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable("userId") Long userId) {
        log.info("Пришел DELETE запрос /users/{}", userId);
        userService.removeUser(userId);
        log.info("Метод DELETE /users/{} успешно выполнен", userId);
    }

}
