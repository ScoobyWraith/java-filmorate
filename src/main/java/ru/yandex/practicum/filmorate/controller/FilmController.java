package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.checkers.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films;
    private final List<IChecker<Film>> checks;

    public FilmController() {
        films = new HashMap<>();
        checks = List.of(
                new FilmNameChecker(),
                new FilmDescriptionChecker(),
                new FilmReleaseDateChecker(),
                new FilmDurationChecker()
        );
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.debug("Adding film: {}", film);

        for (IChecker<Film> checker : checks) {
            try {
                checker.check(film);
            } catch (ValidationException exception) {
                log.warn("Error on adding film", exception);
                return null;
            }
        }

        film.setId(getNextId());
        films.put(film.getId(), film);

        log.info("Film '{}' successfully added", film);

        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.debug("Updating film: {}", film);

        for (IChecker<Film> checker : checks) {
            try {
                checker.check(film);
            } catch (ValidationException exception) {
                log.warn("Error on updating film", exception);
                return null;
            }
        }

        films.put(film.getId(), film);

        log.info("Film '{}' successfully updated", film);

        return film;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
