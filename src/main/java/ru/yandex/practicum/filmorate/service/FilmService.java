package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage storage;

    public Film add(Film film) {
        return null;
    }

    public Film update(Film film) {
        return null;
    }

    public Collection<Film> getAll() {
        return null;
    }

    public void addLike(Long filmId, Long userId) {

    }

    public void removeLike(Long filmId, Long userId) {

    }

    public Collection<Film> getPopularFilms(int size) {
        return null;
    }

    private long getNextId() {
        long currentMaxId = storage.getAll()
                .stream()
                .mapToLong(Film::getId)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
