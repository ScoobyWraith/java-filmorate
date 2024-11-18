package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.checkers.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService service;
    private final List<IChecker<Film>> checks;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
        checks = List.of(
                new FilmNameChecker(),
                new FilmDescriptionChecker(),
                new FilmReleaseDateChecker(),
                new FilmDurationChecker()
        );
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.debug("Request to add film: {}", film);

        for (IChecker<Film> checker : checks) {
            try {
                checker.check(film);
            } catch (ValidationException exception) {
                log.warn("Error on adding film: {}", exception.getMessage());
                throw exception;
            }
        }

        return service.add(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.debug("Request to update film: {}", film);

        for (IChecker<Film> checker : checks) {
            try {
                checker.check(film);
            } catch (ValidationException exception) {
                log.warn("Error on updating film: {}", exception.getMessage());
                throw exception;
            }
        }

        return service.update(film);
    }

    @GetMapping
    public Collection<Film> getAll() {
        return service.getAll();
    }
}
