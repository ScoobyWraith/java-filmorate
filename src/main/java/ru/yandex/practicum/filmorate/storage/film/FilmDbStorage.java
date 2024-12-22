package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFound;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.sql.ResultSet;
import java.util.*;

@Repository
@Qualifier("DBStorage")
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String ADD_NEW_FILM = "INSERT INTO films " +
            "(name, description, release_date, duration, mpa_rating_id) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_FILM_BY_ID = "SELECT * FROM films WHERE film_id = ?";
    private static final String UPDATE_FILM = "UPDATE films SET " +
            "name = ?, description = ?, release_date = ?, duration = ?, mpa_rating_id = ? WHERE film_id = ?";
    private static final String GET_ALL_FILMS = "SELECT * FROM films WHERE film_id = ?";
    private static final String DELETE_FILM = "DELETE FROM films WHERE film_id = ?";

    private static final String GET_GENRES_FOR_FILM = "SELECT genre_id FROM films_genre WHERE film_id = ?";
    private static final String GET_GENRES_FOR_FILMS = "SELECT * FROM films_genre";
    private static final String ADD_GENRE_TO_FILM = "INSERT INTO films_genre (film_id, genre_id) VALUES (?, ?)";
    private static final String REMOVE_GENRE_FROM_FILM = "DELETE films_genre WHERE film_id = ? AND genre_id = ?";

    private static final String GET_LIKES_FOR_FILM = "SELECT user_id FROM film_likes WHERE film_id = ?";
    private static final String GET_LIKES_FOR_FILMS = "SELECT * FROM film_likes";
    private static final String ADD_LIKE_TO_FILM = "INSERT INTO film_likes (film_id, uer_id) VALUES (?, ?)";
    private static final String REMOVE_LIKE_FROM_FILM = "DELETE film_likes WHERE film_id = ? AND user_id = ?";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Film add(Film film) {
        long newId = insert(
                ADD_NEW_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaRating()
        );
        Optional<Film> newFilmOpt = getById(newId);

        if (newFilmOpt.isEmpty()) {
            throw new NotFound("Can't get new film with created id " + newId);
        }

        Film newFilm = newFilmOpt.get();
        film.setId(newFilm.getId());
        updateGenres(film);
        updateLikes(film);
        return newFilm;
    }

    @Override
    public Optional<Film> getById(long id) {
        Optional<Film> filmOpt = findOne(GET_FILM_BY_ID, id);

        if (filmOpt.isEmpty()) {
            return Optional.empty();
        }

        Film film = filmOpt.get();
        List<Integer> genres = jdbc.queryForList(GET_GENRES_FOR_FILM, Integer.class, id);
        List<Long> likes = jdbc.queryForList(GET_LIKES_FOR_FILM, Long.class, id);

        film.setGenres(new HashSet<>(genres));
        film.setUsersWhoLiked(new HashSet<>(likes));

        return Optional.of(film);
    }

    @Override
    public Film update(Film film) {
        update(
                UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaRating(),
                film.getId()
        );
        updateLikes(film);
        updateGenres(film);

        Optional<Film> updatedFilmOpt = getById(film.getId());

        if (updatedFilmOpt.isEmpty()) {
            throw new NotFound("Can't get updated film with created id " + film.getId());
        }

        return updatedFilmOpt.get();
    }

    @Override
    public boolean deleteById(long id) {
        return delete(DELETE_FILM, id);
    }

    @Override
    public Collection<Film> getAll() {
        HashMap<Long, Film> films = getMapWithAllById(GET_ALL_FILMS);

        jdbc.query(GET_LIKES_FOR_FILMS, (ResultSet rs) -> {
            long filmId = rs.getLong("film_id");
            long userId = rs.getLong("user_id");
            films.get(filmId).addLike(userId);
        });

        jdbc.query(GET_GENRES_FOR_FILMS, (ResultSet rs) -> {
            long filmId = rs.getLong("film_id");
            int genreId = rs.getInt("genre_id");
            films.get(filmId).addGenre(genreId);
        });

        return films.values();
    }

    private void updateGenres(Film film) {
        List<Integer> currentGenres = jdbc.queryForList(GET_GENRES_FOR_FILM, Integer.class, film.getId());
        updateSet(film.getId(), currentGenres, film.getGenres(), ADD_GENRE_TO_FILM, REMOVE_GENRE_FROM_FILM);
    }

    private void updateLikes(Film film) {
        List<Long> currentLikes = jdbc.queryForList(GET_LIKES_FOR_FILM, Long.class, film.getId());
        updateSet(film.getId(), currentLikes, film.getUsersWhoLiked(), ADD_LIKE_TO_FILM, REMOVE_LIKE_FROM_FILM);
    }
}
