package ru.yandex.practicum.filmorate.controller.checkers;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;

public class FilmReleaseDateChecker implements IChecker<FilmDto> {
    private final LocalDate startOfFilmsEpoch = LocalDate.of(1895, 12, 28);

    @Override
    public void check(FilmDto model) throws ValidationException {
        final LocalDate releaseDate = model.getReleaseDate();

        if (releaseDate != null && releaseDate.isBefore(startOfFilmsEpoch)) {
            throw new ValidationException("Film's release date before then " + startOfFilmsEpoch);
        }
    }
}
