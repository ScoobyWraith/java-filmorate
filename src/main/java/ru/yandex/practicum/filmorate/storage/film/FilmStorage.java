package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    public Film add(Film film);

    public Optional<Film> getById(long id);

    public Film update(Film film);

    public boolean deleteById(long id);

    public Collection<Film> getAll();
}
