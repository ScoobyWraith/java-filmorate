package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFound;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@Service
public class GenreService {
    private final GenreStorage storage;

    public GenreService(@Qualifier("DBStorage") GenreStorage storage) {
        this.storage = storage;
    }

    public Collection<FilmGenre> getAll() {
        return storage.getAll().values();
    }

    public FilmGenre getById(int id) {
        FilmGenre genre = storage.getById(id);

        if (genre == null) {
            throw new NotFound("Genre with id " + id + " not found");
        }

        return genre;
    }
}
