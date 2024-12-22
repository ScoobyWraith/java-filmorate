package ru.yandex.practicum.filmorate.controller.checkers;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

public class FilmDurationChecker implements IChecker<FilmDto> {
    @Override
    public void check(FilmDto model) throws ValidationException {
        final int duration = model.getDuration();

        if (duration < 1) {
            throw new ValidationException("Film's duration is less then 1");
        }
    }
}
