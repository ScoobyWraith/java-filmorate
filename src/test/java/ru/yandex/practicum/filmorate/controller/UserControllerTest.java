package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.NotFound;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

class UserControllerTest {
    private UserController controller;

    @BeforeEach
    public void createController() {
        UserStorage storage = new InMemoryUserStorage();
        UserService service = new UserService(storage);
        controller = new UserController(service);
    }

    @Test
    public void testAddUser() {
        final User user = User.builder()
                .login("user_login")
                .name("user name")
                .email("user@email.com")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();

        final User userFromController = controller.addUser(user);

        Assertions.assertEquals(user.getName(), userFromController.getName(), "Wrong user name");
        Assertions.assertEquals(user.getLogin(), userFromController.getLogin(), "Wrong user login");
        Assertions.assertEquals(user.getEmail(), userFromController.getEmail(), "Wrong user email");
        Assertions.assertEquals(user.getBirthday(), userFromController.getBirthday(), "Wrong user birth day");
    }

    @Test
    public void testUpdateUser() {
        final User user = User.builder()
                .login("user_login")
                .name("user name")
                .email("user@email.com")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();

        final User userFromController = controller.addUser(user);
        final User updatedUser = userFromController.toBuilder()
                .birthday(LocalDate.of(1970, 12, 12))
                .build();
        final User updatedUserFromController = controller.updateUser(updatedUser);

        Assertions.assertEquals(updatedUser.getName(), updatedUserFromController.getName(), "Wrong user name");
        Assertions.assertEquals(updatedUser.getLogin(), updatedUserFromController.getLogin(), "Wrong user login");
        Assertions.assertEquals(updatedUser.getEmail(), updatedUserFromController.getEmail(), "Wrong user email");
        Assertions.assertEquals(
                updatedUser.getBirthday(),
                updatedUserFromController.getBirthday(),
                "Wrong user birth day"
        );
    }

    @Test
    public void testUpdateUnknownUser() {
        final User user = User.builder()
                .id(999L)
                .login("user_login")
                .name("user name")
                .email("user@email.com")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();

        Assertions.assertThrows(NotFound.class, () -> {
            controller.updateUser(user);
        }, "Can update unknown user");
    }

    @Test
    public void testAddUserWithEmailWithoutAT() {
        final User user = User.builder()
                .login("user_login")
                .name("user name")
                .email("email.com")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> {
            controller.addUser(user);
        }, "Can add user with wrong email");
    }

    @Test
    public void testAddUserWithLoginWithSpaces() {
        final User user = User.builder()
                .login("login with spaces")
                .name("user name")
                .email("user@email.com")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> {
            controller.addUser(user);
        }, "Can add user with login with spaces");
    }

    @Test
    public void testAddUserWithoutName() {
        final User user = User.builder()
                .login("user_login")
                .email("user@email.com")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();
        final User userFromController = controller.addUser(user);

        Assertions.assertEquals(user.getLogin(), userFromController.getName(), "Name was not repeated");
    }

    @Test
    public void testAddUserWithBirthDayInFeature() {
        final User user = User.builder()
                .login("user_login")
                .email("user@email.com")
                .birthday(LocalDate.of(2100, 1, 1))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> {
            controller.addUser(user);
        }, "Can add user with birth day in feature");
    }

    @Test
    public void testGetUsers() {
        final User user1 = User.builder()
                .login("user1")
                .email("user1@email.com")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        final User user2 = user1.toBuilder().login("user2").build();

        controller.addUser(user1);
        controller.addUser(user2);
        Collection<User> users = controller.getAll();

        Assertions.assertEquals(2, users.size(), "Wrong number of users");
    }
}