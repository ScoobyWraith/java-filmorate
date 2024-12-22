package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.NoSuchElementException;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan(basePackages = "ru.yandex.practicum.filmorate")
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    @Test
    public void testAddUser() {
        final User user = User.builder()
                .login("user_login")
                .name("user name")
                .email("user@email.com")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();

        final User userFromController = userStorage.add(user);

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

        final User userFromController = userStorage.add(user);
        final User updatedUser = userFromController.toBuilder()
                .birthday(LocalDate.of(1970, 12, 12))
                .build();
        final User updatedUserFromController = userStorage.update(updatedUser);

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

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            userStorage.update(user);
        }, "Can update unknown user");
    }

    @Test
    public void testGetUsers() {
        final User user1 = User.builder()
                .name("name1")
                .login("user1")
                .email("user1@email.com")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        final User user2 = user1.toBuilder()
                .login("user2")
                .email("user2@email.com")
                .build();

        userStorage.add(user1);
        userStorage.add(user2);
        Collection<User> users = userStorage.getAll();

        Assertions.assertEquals(2, users.size(), "Wrong number of users");
    }
}
