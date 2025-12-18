package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exceptions.NotFound;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    private FilmController controller;
    private MpaStorage mpaStorage;
    private GenreStorage genreStorage;

    @BeforeEach
    public void createController() {
        FilmStorage storage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        FilmService service = new FilmService(storage, genreStorage, mpaStorage, userService);
        controller = new FilmController(service);
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

        FilmDto filmDto = FilmMapper.filmToFilmDto(film);
        final Film filmFromController = FilmMapper.filmDtoToFilm(controller.addFilm(filmDto));

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
                .mpaRating(new MpaRating(1, "G"))
                .build();

        FilmDto filmDto = FilmMapper.filmToFilmDto(film);
        final Film filmFromController = FilmMapper.filmDtoToFilm(controller.addFilm(filmDto));

        final Film updatedFilm = filmFromController.toBuilder()
                .releaseDate(LocalDate.of(1970, 12, 12))
                .build();
        filmDto = FilmMapper.filmToFilmDto(updatedFilm);
        final Film updatedFilmFromController = FilmMapper.filmDtoToFilm(controller.updateFilm(filmDto));

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
                .mpaRating(new MpaRating(1, "G"))
                .build();

        assertThrows(NotFound.class, () -> {
            controller.updateFilm(FilmMapper.filmToFilmDto(film));
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

        assertThrows(ValidationException.class, () -> {
            controller.addFilm(FilmMapper.filmToFilmDto(film));
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

        assertThrows(ValidationException.class, () -> {
            controller.addFilm(FilmMapper.filmToFilmDto(film));
        }, "Can add film with negative duration");
    }

    @Test
    public void testAddFilmWithoutName() {
        final Film film = Film.builder()
                .description("film description")
                .releaseDate(LocalDate.of(1980, 1, 1))
                .mpaRating(new MpaRating(1, "G"))
                .build();
        assertThrows(ValidationException.class, () -> {
            controller.addFilm(FilmMapper.filmToFilmDto(film));
        }, "Can add film without name");
    }

    @Test
    public void testAddFilmWithReleaseDateInPast() {
        final Film film = Film.builder()
                .name("film name")
                .description("film description")
                .releaseDate(LocalDate.of(1880, 1, 1))
                .mpaRating(new MpaRating(1, "G"))
                .build();
        assertThrows(ValidationException.class, () -> {
            controller.addFilm(FilmMapper.filmToFilmDto(film));
        }, "Can add film before than 28.12.1895");
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

        controller.addFilm(FilmMapper.filmToFilmDto(film1));
        controller.addFilm(FilmMapper.filmToFilmDto(film2));
        Collection<FilmDto> films = controller.getAll();

        assertEquals(2, films.size(), "Wrong number of films");
    }
}