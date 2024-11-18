package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.checkers.IChecker;
import ru.yandex.practicum.filmorate.controller.checkers.UserBirthDateChecker;
import ru.yandex.practicum.filmorate.controller.checkers.UserEmailChecker;
import ru.yandex.practicum.filmorate.controller.checkers.UserLoginChecker;
import ru.yandex.practicum.filmorate.exceptions.NotFound;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users;
    private final List<IChecker<User>> checks;

    public UserController() {
        users = new HashMap<>();
        checks = List.of(
                new UserEmailChecker(),
                new UserLoginChecker(),
                new UserBirthDateChecker()
        );
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.debug("Adding user: {}", user);

        for (IChecker<User> checker : checks) {
            try {
                checker.check(user);
            } catch (ValidationException exception) {
                log.warn("Error on adding user: {}", exception.getMessage());
                throw exception;
            }
        }

        setUserNameIfNot(user);
        user.setId(getNextId());
        users.put(user.getId(), user);

        log.info("User '{}' successfully added", user);

        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.debug("Updating user: {}", user);

        for (IChecker<User> checker : checks) {
            try {
                checker.check(user);
            } catch (ValidationException exception) {
                log.warn("Error on updating user: {}", exception.getMessage());
                throw exception;
            }
        }

        Long id = user.getId();

        if (!users.containsKey(id)) {
            log.warn("Error on updating user: user with id {} not found", id);
            throw new NotFound("User not found");
        }

        setUserNameIfNot(user);
        users.put(id, user);

        log.info("User '{}' successfully updated", user);

        return user;
    }

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void setUserNameIfNot(User user) {
        final String name = user.getName();

        if (name == null || name.isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
