package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserInMemoryStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    protected long seq = 0L;

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        Optional<User> curUser = getAllUsers()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
        return curUser;
    }

    @Override
    public User createUser(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;

    }

    @Override
    public User updateUser(Long userId, User user) {
        User updUser = users.get(userId);
        if (user.getName() != null && !user.getName().isEmpty()) {
            updUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            updUser.setEmail(user.getEmail());
        }
        return updUser;
    }

    @Override
    public void removeUser(Long userId) {
        users.remove(userId);
    }

    private long generateId() {
        return ++seq;
    }
}
