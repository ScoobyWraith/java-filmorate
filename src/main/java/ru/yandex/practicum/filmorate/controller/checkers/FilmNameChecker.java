package ru.yandex.practicum.filmorate.controller.checkers;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmNameChecker implements IChecker<Film> {
    @Override
    public void check(Film model) throws ValidationException {
        final String name = model.getName();

        if (name == null || name.isBlank()) {
            throw new ValidationException("Film's name is empty");
        }
    }
}
