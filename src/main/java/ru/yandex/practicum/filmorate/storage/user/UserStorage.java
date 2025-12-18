package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    public User add(User user);

    public Optional<User> getById(long id);

    public User update(User user);

    public boolean deleteById(long id);

    public Collection<User> getAll();
}
