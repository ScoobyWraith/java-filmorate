package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.checkers.IChecker;
import ru.yandex.practicum.filmorate.controller.checkers.UserBirthDateChecker;
import ru.yandex.practicum.filmorate.controller.checkers.UserEmailChecker;
import ru.yandex.practicum.filmorate.controller.checkers.UserLoginChecker;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService service;
    private final List<IChecker<User>> checks;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
        checks = List.of(
                new UserEmailChecker(),
                new UserLoginChecker(),
                new UserBirthDateChecker()
        );
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.debug("Request to add user: {}", user);

        for (IChecker<User> checker : checks) {
            try {
                checker.check(user);
            } catch (ValidationException exception) {
                log.warn("Error on adding user: {}", exception.getMessage());
                throw exception;
            }
        }

        return service.add(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.debug("Request to update user: {}", user);

        for (IChecker<User> checker : checks) {
            try {
                checker.check(user);
            } catch (ValidationException exception) {
                log.warn("Error on updating user: {}", exception.getMessage());
                throw exception;
            }
        }

        return service.update(user);
    }

    @GetMapping
    public Collection<User> getAll() {
        return service.getAll();
    }
}
