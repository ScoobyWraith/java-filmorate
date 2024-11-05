package ru.yandex.practicum.filmorate.controller.checkers;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

public class UserEmailChecker implements IChecker<User> {
    @Override
    public void check(User model) throws ValidationException {
        final String email = model.getEmail();

        if (email == null) {
            throw new ValidationException("User's email is empty");
        }

        if (!email.contains("@")) {
            throw new ValidationException("User's email don't contains symbol '@'");
        }
    }
}
