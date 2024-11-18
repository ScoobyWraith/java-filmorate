package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.NotFound;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    private FilmController controller;

    @BeforeEach
    public void createController() {
        FilmStorage storage = new InMemoryFilmStorage();
        FilmService service = new FilmService(storage);
        controller = new FilmController(service);
    }

    @Test
    public void testAddFilm() {
        final Film film = Film.builder()
                .name("film name")
                .description("film description")
                .duration(120)
                .releaseDate(LocalDate.of(1980, 1, 1))
                .build();

        final Film filmFromController = controller.addFilm(film);

        assertEquals(film.getName(), filmFromController.getName(), "Wrong film name");
        assertEquals(film.getDescription(), filmFromController.getDescription(), "Wrong film description");
        assertEquals(film.getDuration(), filmFromController.getDuration(), "Wrong film duration");
        assertEquals(film.getReleaseDate(), filmFromController.getReleaseDate(), "Wrong film release date");
    }

    @Test
    public void testUpdateFilm() {
        final Film film = Film.builder()
                .name("film name")
                .description("film description")
                .duration(100)
                .releaseDate(LocalDate.of(1980, 1, 1))
                .build();

        final Film filmFromController = controller.addFilm(film);
        final Film updatedFilm = filmFromController.toBuilder()
                .releaseDate(LocalDate.of(1970, 12, 12))
                .build();
        final Film updatedFilmFromController = controller.updateFilm(updatedFilm);

        assertEquals(updatedFilm.getName(), updatedFilmFromController.getName(), "Wrong film name");
        assertEquals(updatedFilm.getDescription(), updatedFilmFromController.getDescription(), "Wrong film description");
        assertEquals(updatedFilm.getDuration(), updatedFilmFromController.getDuration(), "Wrong film duration");
        assertEquals(updatedFilm.getReleaseDate(), updatedFilmFromController.getReleaseDate(), "Wrong film release date");
    }

    @Test
    public void testUpdateUnknownFilm() {
        final Film film = Film.builder()
                .id(999L)
                .name("film name")
                .description("film description")
                .duration(85)
                .releaseDate(LocalDate.of(1980, 1, 1))
                .build();

        assertThrows(NotFound.class, () -> {
            controller.updateFilm(film);
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
                .build();

        assertThrows(ValidationException.class, () -> {
            controller.addFilm(film);
        }, "Can add film with long description");
    }

    @Test
    public void testAddFilmWithNegativeDuration() {
        final Film film = Film.builder()
                .name("Film name")
                .description("film description")
                .duration(-2)
                .releaseDate(LocalDate.of(1980, 1, 1))
                .build();

        assertThrows(ValidationException.class, () -> {
            controller.addFilm(film);
        }, "Can add film with negative duration");
    }

    @Test
    public void testAddFilmWithoutName() {
        final Film film = Film.builder()
                .description("film description")
                .releaseDate(LocalDate.of(1980, 1, 1))
                .build();
        assertThrows(ValidationException.class, () -> {
            controller.addFilm(film);
        }, "Can add film without name");
    }

    @Test
    public void testAddFilmWithReleaseDateInPast() {
        final Film film = Film.builder()
                .name("film name")
                .description("film description")
                .releaseDate(LocalDate.of(1880, 1, 1))
                .build();
        assertThrows(ValidationException.class, () -> {
            controller.addFilm(film);
        }, "Can add film before than 28.12.1895");
    }

    @Test
    public void testGetFilms() {
        final Film film1 = Film.builder()
                .name("film name 1")
                .description("film description")
                .duration(95)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .build();
        final Film film2 = film1.toBuilder().name("film name 1").build();

        controller.addFilm(film1);
        controller.addFilm(film2);
        Collection<Film> films = controller.getAll();

        assertEquals(2, films.size(), "Wrong number of films");
    }
}