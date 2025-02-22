package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> getAllUsers();

    Optional<User> getUserById(Long userId);

    Optional<User> getUserByEmail(String email);

    boolean existEmail(String email);

    User createUser(User user);

    User updateUser(User user);

    void removeUser(Long userId);
}
