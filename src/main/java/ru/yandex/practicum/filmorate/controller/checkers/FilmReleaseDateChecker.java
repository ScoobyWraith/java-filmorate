package ru.yandex.practicum.filmorate.controller.checkers;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FilmReleaseDateChecker implements IChecker<Film> {
    private final Instant startOfFilmsEpoch = LocalDateTime
            .of(1895, 12, 28, 0, 0, 0).toInstant(ZoneOffset.ofHours(0));

    @Override
    public void check(Film model) throws ValidationException {
        final Instant releaseDate = model.getReleaseDate();

        if (releaseDate != null && releaseDate.isBefore(startOfFilmsEpoch)) {
            throw new ValidationException("Film's release date before then " + startOfFilmsEpoch);
        }
    }
}
