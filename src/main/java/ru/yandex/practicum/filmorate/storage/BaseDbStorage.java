package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exceptions.InsertDataException;
import ru.yandex.practicum.filmorate.util.SetUpdateExtractor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseDbStorage<T> {
    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;

    protected Optional<T> findOne(String query, Object... params) {
        try {
            T result = jdbc.queryForObject(query, mapper, params);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    protected List<T> findMany(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }

    protected HashMap<Long, T> getMapWithAllById(String query, String keyName) {
        HashMap<Long, T> result = new HashMap<>();

        jdbc.query(query, (ResultSet rs) -> {
            Long id = rs.getLong(keyName);
            result.put(id, mapper.mapRow(rs, rs.getRow()));
        });

        return result;
    }

    protected long insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    query,
                    Statement.RETURN_GENERATED_KEYS
            );

            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }

            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);

        if (id != null) {
            return id;
        } else {
            throw new InsertDataException("Can't insert data");
        }
    }

    protected boolean update(String query, Object... params) {
        int rowsUpdated = jdbc.update(query, params);
        return rowsUpdated > 0;
    }

    protected boolean delete(String query, long id) {
        return update(query, id);
    }

    protected <U> void updateSet(Long id,
                                 Collection<U> currentSet,
                                 Collection<U> newSet,
                                 String addQuery,
                                 String removeQuery) {
        SetUpdateExtractor<U> updatedSet = new SetUpdateExtractor<>(currentSet, newSet);

        for (U newId : updatedSet.getToAdd()) {
            update(addQuery, id, newId);
        }

        for (U removeId : updatedSet.getToRemove()) {
            update(removeQuery, id, removeId);
        }
    }
}
