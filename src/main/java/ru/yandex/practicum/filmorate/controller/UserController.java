package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.checkers.IChecker;
import ru.yandex.practicum.filmorate.controller.checkers.UserBirthDateChecker;
import ru.yandex.practicum.filmorate.controller.checkers.UserEmailChecker;
import ru.yandex.practicum.filmorate.controller.checkers.UserLoginChecker;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
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
        for (IChecker<User> checker : checks) {
            checker.check(user);
        }

        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        for (IChecker<User> checker : checks) {
            checker.check(user);
        }

        users.put(user.getId(), user);
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
}
