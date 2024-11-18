package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFound;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage storage;

    public Film add(Film film) {
        film.setId(getNextId());
        storage.add(film);

        log.info("Film '{}' successfully added", film);

        return film;
    }

    public Film update(Film film) {
        Long id = film.getId();

        if (storage.getById(id).isEmpty()) {
            log.warn("Error on updating film: film with id {} not found", id);
            throw new NotFound("Film not found");
        }

        storage.update(film);

        log.info("Film '{}' successfully updated", film);

        return film;
    }

    public Collection<Film> getAll() {
        return storage.getAll();
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
