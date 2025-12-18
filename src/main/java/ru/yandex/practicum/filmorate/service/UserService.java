package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFound;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage storage;

    public UserService(@Qualifier("DBStorage") UserStorage storage) {
        this.storage = storage;
    }

    public User getById(Long id) {
        return getWithCheck(id);
    }

    public User add(User user) {
        setUserNameIfNot(user);
        user = storage.add(user);

        log.info("User '{}' successfully added", user);

        return user;
    }

    public User update(User user) {
        getWithCheck(user.getId());
        setUserNameIfNot(user);
        user = storage.update(user);
        log.info("User '{}' successfully updated", user);
        return user;
    }

    public Collection<User> getAll() {
        return storage.getAll();
    }

    public void addFriend(Long userId, Long friendId) {
        User user = getWithCheck(userId);
        getWithCheck(friendId);

        user.addFriend(friendId);
        storage.update(user);

        log.info("User {} add user {} into friend list", userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = getWithCheck(userId);
        getWithCheck(friendId);

        user.removeFriend(friendId);
        storage.update(user);

        log.info("User {} removed user {} from friend list", userId, friendId);
    }

    public Set<User> getFriends(Long userId) {
        User user = getWithCheck(userId);
        Set<Long> userFriends = user.getFriends();

        return userFriends == null
                ? new HashSet<>()
                : userFriends.stream()
                .map(id -> storage.getById(id).orElseThrow())
                .collect(Collectors.toSet());
    }

    public Collection<User> getCommonFriends(Long userId, Long anotherUserId) {
        User user = getWithCheck(userId);
        User anotherUser = getWithCheck(anotherUserId);

        Set<Long> userFriends = user.getFriends();
        Set<Long> anotherUserFriends = anotherUser.getFriends();

        if (userFriends == null || anotherUserFriends == null) {
            return new HashSet<>();
        }

        return userFriends.stream()
                .filter(anotherUserFriends::contains)
                .map(id -> storage.getById(id).orElseThrow())
                .collect(Collectors.toSet());
    }

    public User getWithCheck(long id) throws NotFound {
        Optional<User> userOpt = storage.getById(id);

        if (userOpt.isEmpty()) {
            log.warn("User with id {} not found", id);
            throw new NotFound("User with id " + id + " not found");
        }

        return userOpt.get();
    }

    private void setUserNameIfNot(User user) {
        final String name = user.getName();

        if (name == null || name.isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
