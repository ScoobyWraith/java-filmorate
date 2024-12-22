package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFound;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.sql.ResultSet;
import java.util.*;

@Repository
@Qualifier("DBStorage")
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private static final String ADD_NEW_USER = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String GET_USER_BY_ID = "SELECT * FROM users WHERE user_id = ?";
    private static final String UPDATE_USER = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? " +
            "WHERE user_id = ?";
    private static final String DELETE_USER = "DELETE FROM users WHERE user_id = ?";
    private static final String GET_ALL_USERS = "SELECT * FROM users";

    private static final String GET_FRIENDS_FOR_USER = "SELECT receiver FROM friendship WHERE sender = ?";
    private static final String GET_FRIENDS_FOR_USERS = "SELECT * FROM friendship";
    private static final String ADD_FRIEND_TO_USER = "INSERT INTO friendship (sender, receiver) VALUES (?, ?)";
    private static final String REMOVE_FRIEND_FROM_USER = "DELETE FROM friendship WHERE sender = ? AND receiver = ?";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public User add(User user) {
        long newId = insert(ADD_NEW_USER, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        Optional<User> newUserOpt = getById(newId);

        if (newUserOpt.isEmpty()) {
            throw new NotFound("Can't get new user with created id " + newId);
        }

        return newUserOpt.get();
    }

    @Override
    public Optional<User> getById(long id) {
        Optional<User> userOpt = findOne(GET_USER_BY_ID, id);

        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();
        List<Long> friends = jdbc.queryForList(GET_FRIENDS_FOR_USER, Long.class, id);
        user.setFriends(new HashSet<>(friends));
        return Optional.of(user);
    }

    @Override
    public Collection<User> getAll() {
        HashMap<Long, User> users = getMapWithAllById(GET_ALL_USERS);

        jdbc.query(GET_FRIENDS_FOR_USERS, (ResultSet rs) -> {
            long sender = rs.getLong("sender");
            long receiver = rs.getLong("receiver");
            users.get(sender).addFriend(receiver);
        });

        return users.values();
    }

    @Override
    public User update(User user) {
        update(UPDATE_USER, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        updateFriends(user);

        Optional<User> updatedUserOpt = getById(user.getId());

        if (updatedUserOpt.isEmpty()) {
            throw new NotFound("Can't get updated user with created id " + user.getId());
        }

        return updatedUserOpt.get();
    }

    @Override
    public boolean deleteById(long id) {
        return delete(DELETE_USER, id);
    }

    private void updateFriends(User user) {
        List<Long> currentFriends = jdbc.queryForList(GET_FRIENDS_FOR_USER, Long.class, user.getId());
        updateSet(user.getId(), currentFriends, user.getFriends(), ADD_FRIEND_TO_USER, REMOVE_FRIEND_FROM_USER);
    }
}
