package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.checkers.FilmDescriptionChecker;
import ru.yandex.practicum.filmorate.controller.checkers.FilmDurationChecker;
import ru.yandex.practicum.filmorate.controller.checkers.FilmNameChecker;
import ru.yandex.practicum.filmorate.controller.checkers.FilmReleaseDateChecker;
import ru.yandex.practicum.filmorate.controller.checkers.IChecker;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService service;
    private final List<IChecker<FilmDto>> checks;

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
    public FilmDto addFilm(@RequestBody FilmDto film) {
        log.info("Request to add film: {}", film);

        doChecks(film);
        return service.add(film);
    }

    @PutMapping
    public FilmDto updateFilm(@RequestBody FilmDto film) {
        log.info("Request to update film: {}", film);

        doChecks(film);
        return service.update(film);
    }

    @GetMapping
    public Collection<FilmDto> getAll() {
        log.info("Request to get all films");

        return service.getAll();
    }

    @GetMapping("/{id}")
    public FilmDto getFilm(@PathVariable Long id) {
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
    public Collection<FilmDto> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Request to get {} popular films", count);

        return service.getPopularFilms(count);
    }

    private void doChecks(FilmDto film) throws ValidationException {
        for (IChecker<FilmDto> checker : checks) {
            try {
                checker.check(film);
            } catch (ValidationException exception) {
                log.warn("Error with film: {}", exception.getMessage());
                throw exception;
            }
        }
    }
}
