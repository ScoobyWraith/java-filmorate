package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Map;

public class InMemoryMpaStorage implements MpaStorage {
    private final Map<Integer, MpaRating> mpa = Map.of(
            1, new MpaRating(1, "Комедия"),
            2, new MpaRating(2, "Драма"),
            3, new MpaRating(3, "Мультфильм"),
            4, new MpaRating(4, "Триллер"),
            5, new MpaRating(5, "Документальный")
    );

    @Override
    public Map<Integer, MpaRating> getAll() {
        return mpa;
    }

    @Override
    public MpaRating getById(int id) {
        return mpa.get(id);
    }
}
