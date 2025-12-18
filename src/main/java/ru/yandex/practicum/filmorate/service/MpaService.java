package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFound;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;

@Service
public class MpaService {
    private final MpaStorage storage;

    public MpaService(@Qualifier("DBStorage") MpaStorage storage) {
        this.storage = storage;
    }

    public Collection<MpaRating> getAll() {
        return storage.getAll().values();
    }

    public MpaRating getById(int id) {
        MpaRating mpa = storage.getById(id);

        if (mpa == null) {
            throw new NotFound("MPA with id " + id + " not found");
        }

        return mpa;
    }
}
