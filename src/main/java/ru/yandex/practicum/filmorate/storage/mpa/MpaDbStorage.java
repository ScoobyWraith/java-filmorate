package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@Repository
@Qualifier("DBStorage")
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private static final String GET_ALL_MPA = "SELECT * FROM mpa_rating";
    private final JdbcTemplate jdbc;
    private final Map<Integer, MpaRating> mpa = new HashMap<>();

    @Override
    public Map<Integer, MpaRating> getAll() {
        createAllMpaRatingIfNeeded();
        return mpa;
    }

    @Override
    public MpaRating getById(int id) {
        createAllMpaRatingIfNeeded();
        return mpa.get(id);
    }

    private void createAllMpaRatingIfNeeded() {
        if (!mpa.isEmpty()) {
            return;
        }

        jdbc.query(GET_ALL_MPA, (ResultSet rs) -> {
            Integer mpa_id = rs.getInt("mpa_rating_id");
            String name = rs.getString("name");
            mpa.put(mpa_id, new MpaRating(mpa_id, name));
        });
    }
}
