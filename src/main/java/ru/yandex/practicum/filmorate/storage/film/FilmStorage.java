package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    public void add(Film film);

    public Optional<Film> getById(long id);

    public void update(Film film);

    public void deleteById(long id);

    public Collection<Film> getAll();
}
