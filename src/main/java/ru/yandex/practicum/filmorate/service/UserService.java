package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;

    public User add(User user) {
        return null;
    }

    public User update(User user) {
        return null;
    }

    public Collection<User> getAll() {
        return null;
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
}
