package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = :id";
    private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM users WHERE email = :email";
    private static final String INSERT_QUERY = "INSERT INTO users(name, email) VALUES (:name, :email)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = :email, name = :name WHERE id = :id";

    @Override
    public Collection<User> getAllUsers() {
        return List.of();
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return null;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return null;
    }

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public User updateUser(Long userId, User user) {
        return null;
    }

    @Override
    public void removeUser(Long userId) {

    }
}
