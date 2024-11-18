package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> storage = new HashMap<>();

    @Override
    public void add(Film film) {
        storage.put(film.getId(), film);
    }

    @Override
    public Optional<Film> getById(long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void update(Film film) {
        storage.put(film.getId(), film);
    }

    @Override
    public void deleteById(long id) {
        storage.remove(id);
    }

    @Override
    public List<Film> getAll() {
        return storage.values().stream().toList();
    }
}
