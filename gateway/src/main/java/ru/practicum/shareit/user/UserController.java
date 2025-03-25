package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient userClient;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUser(@PathVariable("id") Long userId) {
        log.info("Gateway запрос GET /users/{}", userId);
        ResponseEntity<Object> user = userClient.getUser(userId);
        log.info("Gateway ответ GET /users/{} вернул {}", userId, user);
        return user;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserCreateDto userDto) {
        log.info("Gateway запрос POST /users с телом {}", userDto);
        ResponseEntity<Object> newUser = userClient.createUser(userDto);
        log.info("Gateway ответ POST /users вернул {}", newUser);
        return newUser;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateUser(@PathVariable("id") Long userId,
                                             @RequestBody UserUpdateDto userDto) {
        log.info("Gateway запрос PATCH /users с телом {} и id={}", userDto, userId);
        ResponseEntity<Object> newUser = userClient.updateUser(userId, userDto);
        log.info("Gateway ответ PATCH /users вернул {}", newUser);
        return newUser;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> removeUser(@PathVariable("userId") Long userId) {
        log.info("Gateway запрос DELETE /users/{}", userId);
        ResponseEntity<Object> result = userClient.deleteUser(userId);
        log.info("Gateway ответ DELETE /users/{} успешно выполнен", userId);
        return result;
    }

}
