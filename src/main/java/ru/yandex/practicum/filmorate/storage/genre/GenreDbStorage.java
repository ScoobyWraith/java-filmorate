package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@Repository
@Qualifier("DBStorage")
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private static final String GET_ALL_GENRES = "SELECT * FROM genre";
    private final JdbcTemplate jdbc;
    private final Map<Integer, FilmGenre> genres = new HashMap<>();

    @Override
    public Map<Integer, FilmGenre> getAllGenres() {
        createGenresIfNeeded();
        return genres;
    }

    @Override
    public FilmGenre getById(int id) {
        createGenresIfNeeded();
        return genres.get(id);
    }

    private void createGenresIfNeeded() {
        if (!genres.isEmpty()) {
            return;
        }

        jdbc.query(GET_ALL_GENRES, (ResultSet rs) -> {
            Integer genre_id = rs.getInt("genre_id");
            String name = rs.getString("name");
            genres.put(genre_id, new FilmGenre(genre_id, name));
        });
    }
}
