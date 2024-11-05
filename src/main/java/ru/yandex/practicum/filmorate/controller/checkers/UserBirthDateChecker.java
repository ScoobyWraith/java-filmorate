package ru.yandex.practicum.filmorate.controller.checkers;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;

public class UserBirthDateChecker implements IChecker<User> {
    @Override
    public void check(User model) throws ValidationException {
        final Instant birthDate = model.getBirthday();

        if (birthDate != null && birthDate.isAfter(Instant.now())) {
            throw new ValidationException("User's birth date in feature");
        }
    }
}
