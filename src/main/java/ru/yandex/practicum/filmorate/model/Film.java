package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Long> usersWhoLiked;
    private Set<Integer> genres;
    private int mpaRating;

    public void addLike(Long userId) {
        if (usersWhoLiked == null) {
            usersWhoLiked = new HashSet<>();
        }

        usersWhoLiked.add(userId);
    }

    public void removeLike(Long userId) {
        if (usersWhoLiked == null) {
            return;
        }

        usersWhoLiked.remove(userId);
    }

    public void addGenre(Integer genreId) {
        if (genres == null) {
            genres = new HashSet<>();
        }

        genres.add(genreId);
    }

    public void removeLike(Integer genreId) {
        if (genres == null) {
            return;
        }

        genres.remove(genreId);
    }

    public int getLikedUsersQuantity() {
        return usersWhoLiked == null ? 0 : usersWhoLiked.size();
    }
}
