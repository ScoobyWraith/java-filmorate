package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.Map;

public class InMemoryGenreStorage implements GenreStorage {
    private final Map<Integer, FilmGenre> genres = Map.of(
            1, new FilmGenre(1, "Комедия"),
            2, new FilmGenre(2, "Драма"),
            3, new FilmGenre(3, "Мультфильм"),
            4, new FilmGenre(4, "Триллер"),
            5, new FilmGenre(5, "Документальный"),
            6, new FilmGenre(6, "Боевик")
    );

    @Override
    public Map<Integer, FilmGenre> getAll() {
        return genres;
    }

    @Override
    public FilmGenre getById(int id) {
        return genres.get(id);
    }
}
