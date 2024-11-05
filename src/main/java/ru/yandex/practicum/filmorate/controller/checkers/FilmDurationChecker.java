package ru.yandex.practicum.filmorate.controller.checkers;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;

public class FilmDurationChecker implements IChecker<Film> {
    @Override
    public void check(Film model) throws ValidationException {
        final Duration duration = model.getDuration();

        if (duration != null && duration.isNegative()) {
            throw new ValidationException("Film's duration is negative");
        }
    }
}
