package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> storage = new HashMap<>();

    @Override
    public void add(User user) {
        storage.put(user.getId(), user);
    }

    @Override
    public Optional<User> getById(long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void update(User user) {
        storage.put(user.getId(), user);
    }

    @Override
    public void deleteById(long id) {
        storage.remove(id);
    }

    @Override
    public List<User> getAll() {
        return storage.values().stream().toList();
    }
}
