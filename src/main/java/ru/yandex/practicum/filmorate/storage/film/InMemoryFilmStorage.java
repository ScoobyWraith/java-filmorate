package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> storage = new HashMap<>();

    @Override
    public void add(Film film) {
        storage.put(film.getId(), film.toBuilder().build());
    }

    @Override
    public Optional<Film> getById(long id) {
        if (!storage.containsKey(id)) {
            return Optional.empty();
        }

        return Optional.of(storage.get(id).toBuilder().build());
    }

    @Override
    public void update(Film film) {
        storage.put(film.getId(), film.toBuilder().build());
    }

    @Override
    public void deleteById(long id) {
        storage.remove(id);
    }

    @Override
    public Collection<Film> getAll() {
        return storage.values();
    }
}
