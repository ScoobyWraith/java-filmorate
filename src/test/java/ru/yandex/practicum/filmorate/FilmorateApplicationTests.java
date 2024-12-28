package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan(basePackages = "ru.yandex.practicum.filmorate")
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final GenreDbStorage genreStorage;
    private final MpaDbStorage mpaStorage;

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

    @Test
    public void testGetUser() {
        final User user1 = User.builder()
                .name("name1")
                .login("user1")
                .email("user1@email.com")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        User createdUser = userStorage.add(user1);
        Optional<User> userFromStorageOpt = userStorage.getById(createdUser.getId());

        Assertions.assertTrue(userFromStorageOpt.isPresent(), "User not found");

        User userFromStorage = userFromStorageOpt.get();

        Assertions.assertEquals("user1", userFromStorage.getLogin(), "User has wrong login");
    }

    @Test
    public void testAddFilm() {
        final Film film = Film.builder()
                .name("film name")
                .description("film description")
                .duration(120)
                .releaseDate(LocalDate.of(1980, 1, 1))
                .mpaRating(new MpaRating(1, "G"))
                .build();

        final Film filmFromStorage = filmStorage.add(film);

        assertEquals(film.getName(), filmFromStorage.getName(), "Wrong film name");
        assertEquals(film.getDescription(), filmFromStorage.getDescription(), "Wrong film description");
        assertEquals(film.getDuration(), filmFromStorage.getDuration(), "Wrong film duration");
        assertEquals(film.getReleaseDate(), filmFromStorage.getReleaseDate(), "Wrong film release date");
    }

    @Test
    public void testUpdateFilm() {
        final Film film = Film.builder()
                .name("film name")
                .description("film description")
                .duration(100)
                .releaseDate(LocalDate.of(1980, 1, 1))
                .mpaRating(new MpaRating(1, "G"))
                .build();

        final Film filmFromStorage = filmStorage.add(film);
        final Film updatedFilm = filmFromStorage.toBuilder()
                .releaseDate(LocalDate.of(1970, 12, 12))
                .build();
        final Film updatedFilmFromStorage = filmStorage.update(updatedFilm);

        assertEquals(updatedFilm.getName(), updatedFilmFromStorage.getName(), "Wrong film name");
        assertEquals(updatedFilm.getDescription(), updatedFilmFromStorage.getDescription(), "Wrong film description");
        assertEquals(updatedFilm.getDuration(), updatedFilmFromStorage.getDuration(), "Wrong film duration");
        assertEquals(updatedFilm.getReleaseDate(), updatedFilmFromStorage.getReleaseDate(), "Wrong film release date");
    }

    @Test
    public void testUpdateUnknownFilm() {
        final Film film = Film.builder()
                .id(999L)
                .name("film name")
                .description("film description")
                .duration(85)
                .releaseDate(LocalDate.of(1980, 1, 1))
                .mpaRating(new MpaRating(1, "G"))
                .build();

        assertThrows(NoSuchElementException.class, () -> {
            filmStorage.update(film);
        }, "Can update unknown film");
    }

    @Test
    public void testAddFilmWithLongDescription() {
        final Film film = Film.builder()
                .name("Film name")
                .description("Very long description ................................................................." +
                        "...................................................................................." +
                        "....................................................................................")
                .releaseDate(LocalDate.of(1980, 1, 1))
                .mpaRating(new MpaRating(1, "G"))
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            filmStorage.add(film);
        }, "Can add film with long description");
    }

    @Test
    public void testAddFilmWithNegativeDuration() {
        final Film film = Film.builder()
                .name("Film name")
                .description("film description")
                .duration(-2)
                .releaseDate(LocalDate.of(1980, 1, 1))
                .mpaRating(new MpaRating(1, "G"))
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            filmStorage.add(film);
        }, "Can add film with negative duration");
    }

    @Test
    public void testAddFilmWithoutName() {
        final Film film = Film.builder()
                .description("film description")
                .releaseDate(LocalDate.of(1980, 1, 1))
                .mpaRating(new MpaRating(1, "G"))
                .build();
        assertThrows(DataIntegrityViolationException.class, () -> {
            filmStorage.add(film);
        }, "Can add film without name");
    }

    @Test
    public void testGetFilms() {
        final Film film1 = Film.builder()
                .name("film name 1")
                .description("film description")
                .duration(95)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .mpaRating(new MpaRating(1, "G"))
                .build();
        final Film film2 = film1.toBuilder().name("film name 1").build();

        filmStorage.add(film1);
        filmStorage.add(film2);
        Collection<Film> films = filmStorage.getAll();

        assertEquals(2, films.size(), "Wrong number of films");
    }

    @Test
    public void testGetFilm() {
        final Film film = Film.builder()
                .name("film name 1")
                .description("film description")
                .duration(95)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .mpaRating(new MpaRating(1, "G"))
                .build();

        Film createdFilm = filmStorage.add(film);
        Optional<Film> filmFromStorageOpt = filmStorage.getById(createdFilm.getId());

        assertTrue(filmFromStorageOpt.isPresent(), "Film not found");

        Film filmFromStorage = filmFromStorageOpt.get();

        assertEquals("film name 1", filmFromStorage.getName(), "Film has wrong name");
    }

    @Test
    public void testGetGenre() {
        FilmGenre genre = genreStorage.getById(3);

        assertEquals("Мультфильм", genre.getName(), "Wrong genre name");
    }

    @Test
    public void testGetGenres() {
        Map<Integer, FilmGenre> genres = genreStorage.getAll();

        assertEquals(6, genres.size(), "Wrong genres size");
    }

    @Test
    public void testGetMpa() {
        MpaRating mpa = mpaStorage.getById(4);

        assertEquals("R", mpa.getName(), "Wrong map name");
    }

    @Test
    public void testGetMpaRatings() {
        Map<Integer, MpaRating> mpaRatings = mpaStorage.getAll();

        assertEquals(5, mpaRatings.size(), "Wrong mpa size");
    }
}
