package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.util.Set;

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
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user) {
        log.info("Request to add user: {}", user);

        doChecks(user);
        return service.add(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Request to update user: {}", user);

        doChecks(user);
        return service.update(user);
    }

    @GetMapping
    public Collection<User> getAll() {
        log.info("Request to get all users");

        return service.getAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        log.info("Request to get user {}", id);

        return service.getById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Request to add friend {} for user {}", friendId, id);

        service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Request to remove friend {} for user {}", friendId, id);

        service.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<User> getFriends(@PathVariable Long id) {
        log.info("Request to get friends for user {}", id);

        return service.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Request to get common friends for user {} and {}", id, otherId);

        return service.getCommonFriends(id, otherId);
    }

    private void doChecks(User user) throws ValidationException {
        for (IChecker<User> checker : checks) {
            try {
                checker.check(user);
            } catch (ValidationException exception) {
                log.warn("Error with user: {}", exception.getMessage());
                throw exception;
            }
        }
    }
}
