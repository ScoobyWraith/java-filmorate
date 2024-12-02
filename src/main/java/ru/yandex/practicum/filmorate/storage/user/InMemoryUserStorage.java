package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> storage = new HashMap<>();

    @Override
    public void add(User user) {
        storage.put(user.getId(), user.toBuilder().build());
    }

    @Override
    public Optional<User> getById(long id) {
        if (!storage.containsKey(id)) {
            return Optional.empty();
        }

        return Optional.of(storage.get(id).toBuilder().build());
    }

    @Override
    public void update(User user) {
        storage.put(user.getId(), user.toBuilder().build());
    }

    @Override
    public void deleteById(long id) {
        storage.remove(id);
    }

    @Override
    public Collection<User> getAll() {
        return storage.values();
    }
}
