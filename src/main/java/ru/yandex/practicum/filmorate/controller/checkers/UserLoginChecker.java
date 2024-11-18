package ru.yandex.practicum.filmorate.controller.checkers;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

public class UserLoginChecker implements IChecker<User> {
    @Override
    public void check(User model) throws ValidationException {
        final String login = model.getLogin();

        if (login == null || login.isBlank()) {
            throw new ValidationException("User's login is empty");
        }

        if (login.contains(" ")) {
            throw new ValidationException("User's login has spaces");
        }
    }
}
