package ru.yandex.practicum.filmorate.controller.checkers;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

public class FilmDescriptionChecker implements IChecker<FilmDto> {
    @Override
    public void check(FilmDto model) throws ValidationException {
        final String desc = model.getDescription();

        if (desc != null && desc.length() > 200) {
            throw new ValidationException("Film's description length more then 200 symbols");
        }
    }
}
