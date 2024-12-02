package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFound;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage storage;

    public User getById(Long id) {
        checkExisting(id);
        return storage.getById(id).orElseThrow();
    }

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
        checkExisting(userId);
        checkExisting(friendId);

        User user = storage.getById(userId).orElseThrow();
        user.addFriend(friendId);
        storage.update(user);

        User friend = storage.getById(friendId).orElseThrow();
        friend.addFriend(userId);
        storage.update(friend);

        log.info("Users {} and {} are friends}", userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        checkExisting(userId);
        checkExisting(friendId);

        User user = storage.getById(userId).orElseThrow();
        user.removeFriend(friendId);
        storage.update(user);

        User friend = storage.getById(friendId).orElseThrow();
        friend.removeFriend(userId);
        storage.update(friend);

        log.info("Users {} and {} are not friends}", userId, friendId);
    }

    public Set<User> getFriends(Long userId) {
        checkExisting(userId);

        User user = storage.getById(userId).orElseThrow();
        Set<Long> userFriends = user.getFriends();

        return userFriends == null
                ? new HashSet<>()
                : userFriends.stream()
                .map(id -> storage.getById(id).orElseThrow())
                .collect(Collectors.toSet());
    }

    public Collection<User> getCommonFriends(Long userId, Long anotherUserId) {
        checkExisting(userId);
        checkExisting(anotherUserId);

        User user = storage.getById(userId).orElseThrow();
        Set<Long> userFriends = user.getFriends();

        User anotherUser = storage.getById(anotherUserId).orElseThrow();
        Set<Long> anotherUserFriends = anotherUser.getFriends();

        if (userFriends == null || anotherUserFriends == null) {
            return new HashSet<>();
        }

        return userFriends.stream()
                .filter(anotherUserFriends::contains)
                .map(id -> storage.getById(id).orElseThrow())
                .collect(Collectors.toSet());
    }

    public void checkExisting(long id) throws NotFound {
        if (storage.getById(id).isEmpty()) {
            log.warn("User with id {} not found", id);
            throw new NotFound("User with id " + id + " not found");
        }
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
