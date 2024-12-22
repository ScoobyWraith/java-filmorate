package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exceptions.NotFound;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage storage;
    private final UserService userService;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    public FilmService(@Qualifier("DBStorage") FilmStorage storage,
                       @Qualifier("DBStorage") GenreStorage genreStorage,
                       @Qualifier("DBStorage") MpaStorage mpaStorage,
                       UserService userService) {
        this.storage = storage;
        this.userService = userService;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    public FilmDto getById(Long id) {
        return FilmMapper.filmToFilmDto(getWithCheck(id), genreStorage, mpaStorage);
    }

    public FilmDto add(FilmDto filmDto) {
        Film film = FilmMapper.filmDtoToFilm(filmDto);
        film = storage.add(film);
        log.info("Film '{}' successfully added", film);

        return FilmMapper.filmToFilmDto(film, genreStorage, mpaStorage);
    }

    public FilmDto update(FilmDto filmDto) {
        Film film = getWithCheck(filmDto.getId());
        film = storage.update(FilmMapper.filmDtoToFilm(filmDto));
        log.info("Film '{}' successfully updated", film);

        return FilmMapper.filmToFilmDto(film, genreStorage, mpaStorage);
    }

    public Collection<FilmDto> getAll() {
        return storage.getAll().stream()
                .map(film -> FilmMapper.filmToFilmDto(film, genreStorage, mpaStorage))
                .toList();
    }

    public void addLike(Long filmId, Long userId) {
        Film film = getWithCheck(filmId);
        userService.getWithCheck(userId);
        film.addLike(userId);
        storage.update(film);

        log.info("User {} liked film {}", userId, filmId);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = getWithCheck(filmId);
        userService.getWithCheck(userId);
        film.removeLike(userId);
        storage.update(film);

        log.info("User {} unliked film {}", filmId, userId);
    }

    public Collection<FilmDto> getPopularFilms(int size) {
        return storage.getAll()
                .stream()
                .sorted((f1, f2) -> f2.getLikedUsersQuantity() - f1.getLikedUsersQuantity())
                .limit(size)
                .map(film -> FilmMapper.filmToFilmDto(film, genreStorage, mpaStorage))
                .toList();
    }

    private Film getWithCheck(long id) throws NotFound {
        Optional<Film> filmOpt = storage.getById(id);

        if (filmOpt.isEmpty()) {
            log.warn("Film with id {} not found", id);
            throw new NotFound("Film with id " + id + " not found");
        }

        return filmOpt.get();
    }
}
