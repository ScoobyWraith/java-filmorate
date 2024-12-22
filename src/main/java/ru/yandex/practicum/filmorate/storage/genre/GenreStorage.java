package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.Map;

public interface GenreStorage {
    public Map<Integer, FilmGenre> getAllGenres();

    public FilmGenre getById(int id);
}
