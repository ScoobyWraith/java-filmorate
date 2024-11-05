package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.checkers.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
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
        for (IChecker<Film> checker : checks) {
            checker.check(film);
        }

        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        for (IChecker<Film> checker : checks) {
            checker.check(film);
        }

        films.put(film.getId(), film);
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
