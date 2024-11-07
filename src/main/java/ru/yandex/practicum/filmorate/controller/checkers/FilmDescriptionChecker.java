package ru.yandex.practicum.filmorate.controller.checkers;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmDescriptionChecker implements IChecker<Film> {
    @Override
    public void check(Film model) throws ValidationException {
        final String desc = model.getDescription();

        if (desc != null && desc.length() > 200) {
            throw new ValidationException("Film's description length more then 200 symbols");
        }
    }
}
