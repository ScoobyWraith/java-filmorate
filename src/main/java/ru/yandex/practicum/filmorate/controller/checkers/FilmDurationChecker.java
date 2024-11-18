package ru.yandex.practicum.filmorate.controller.checkers;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmDurationChecker implements IChecker<Film> {
    @Override
    public void check(Film model) throws ValidationException {
        final int duration = model.getDuration();

        if (duration < 1) {
            throw new ValidationException("Film's duration is less then 1");
        }
    }
}
