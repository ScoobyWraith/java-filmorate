package ru.yandex.practicum.filmorate.controller.checkers;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

public class FilmNameChecker implements IChecker<FilmDto> {
    @Override
    public void check(FilmDto model) throws ValidationException {
        final String name = model.getName();

        if (name == null || name.isBlank()) {
            throw new ValidationException("Film's name is empty");
        }
    }
}
