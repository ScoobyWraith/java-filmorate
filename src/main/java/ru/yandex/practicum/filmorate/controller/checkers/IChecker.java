package ru.yandex.practicum.filmorate.controller.checkers;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;

public interface IChecker<T> {
    void check(T model) throws ValidationException;
}
