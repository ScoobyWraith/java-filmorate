package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Map;

public interface MpaStorage {
    public Map<Integer, MpaRating> getAllMpaRatings();

    public MpaRating getById(int id);
}
