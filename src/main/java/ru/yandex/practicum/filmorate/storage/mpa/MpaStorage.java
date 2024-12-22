package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Map;

public interface MpaStorage {
    public Map<Integer, MpaRating> getAll();

    public MpaRating getById(int id);
}
