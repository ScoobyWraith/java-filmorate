package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@RequestBody Film film) {
        log.info("Request to add film: {}", film);

        doChecks(film);
        return service.add(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Request to update film: {}", film);

        doChecks(film);
        return service.update(film);
    }

    @GetMapping
    public Collection<Film> getAll() {
        log.info("Request to get all films");

        return service.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {
        log.info("Request to get film {}", id);

        return service.getById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Request to add like to film {} from user {}", id, userId);

        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Request to remove like to film {} from user {}", id, userId);

        service.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Request to get {} popular films", count);

        return service.getPopularFilms(count);
    }

    private void doChecks(Film film) throws ValidationException {
        for (IChecker<Film> checker : checks) {
            try {
                checker.check(film);
            } catch (ValidationException exception) {
                log.warn("Error with film: {}", exception.getMessage());
                throw exception;
            }
        }
    }
}
