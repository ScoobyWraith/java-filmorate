package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@Slf4j
@RequiredArgsConstructor
public class GenreController {
    private final GenreService service;

    @GetMapping
    public Collection<FilmGenre> getAll() {
        log.info("Request to get all genre");

        return service.getAll();
    }

    @GetMapping("/{id}")
    public FilmGenre getById(@PathVariable Integer id) {
        log.info("Request to get mpa {}", id);

        return service.getById(id);
    }
}
