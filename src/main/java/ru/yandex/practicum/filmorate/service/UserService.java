package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFound;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage storage;

    public User add(User user) {
        setUserNameIfNot(user);
        user.setId(getNextId());
        storage.add(user);

        log.info("User '{}' successfully added", user);

        return user;
    }

    public User update(User user) {
        Long id = user.getId();

        if (storage.getById(id).isEmpty()) {
            log.warn("Error on updating user: user with id {} not found", id);
            throw new NotFound("User not found");
        }

        setUserNameIfNot(user);
        storage.update(user);

        log.info("User '{}' successfully updated", user);

        return user;
    }

    public Collection<User> getAll() {
        return storage.getAll();
    }

    public void addFriend(Long userId, Long friendId) {

    }

    public void removeFriend(Long userId, Long friendId) {

    }

    public Collection<User> getMutualFriends(Long userId, Long anotherUserId) {
        return null;
    }

    private long getNextId() {
        long currentMaxId = storage.getAll()
                .stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void setUserNameIfNot(User user) {
        final String name = user.getName();

        if (name == null || name.isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
