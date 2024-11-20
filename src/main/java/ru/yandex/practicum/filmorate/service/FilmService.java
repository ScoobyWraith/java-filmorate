package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFound;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage storage;
    private final UserService userService;

    public Film getById(Long id) {
        checkExisting(id);
        return storage.getById(id).orElseThrow();
    }

    public Film add(Film film) {
        film.setId(getNextId());
        storage.add(film);
        log.info("Film '{}' successfully added", film);

        return film;
    }

    public Film update(Film film) {
        Long id = film.getId();
        checkExisting(id);
        storage.update(film);
        log.info("Film '{}' successfully updated", film);

        return film;
    }

    public Collection<Film> getAll() {
        return storage.getAll();
    }

    public void addLike(Long filmId, Long userId) {
        checkExisting(filmId);
        userService.checkExisting(userId);
        Film film = storage.getById(filmId).orElseThrow();
        film.addLike(userId);
        storage.update(film);

        log.info("User {} liked film {}", userId, filmId);
    }

    public void removeLike(Long filmId, Long userId) {
        checkExisting(filmId);
        userService.checkExisting(userId);
        Film film = storage.getById(filmId).orElseThrow();
        film.removeLike(userId);
        storage.update(film);

        log.info("User {} unliked film {}", filmId, userId);
    }

    public Collection<Film> getPopularFilms(int size) {
        List<Film> result = storage.getAll()
                .stream()
                .sorted((f1, f2) -> f2.getLikedUsersQuantity() - f1.getLikedUsersQuantity())
                .toList();
        size = Integer.min(size, result.size());
        return result.subList(0, size);
    }

    private long getNextId() {
        long currentMaxId = storage.getAll()
                .stream()
                .mapToLong(Film::getId)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void checkExisting(long id) throws NotFound {
        if (storage.getById(id).isEmpty()) {
            log.warn("Film with id {} not found", id);
            throw new NotFound("Film with id " + id + " not found");
        }
    }
}
