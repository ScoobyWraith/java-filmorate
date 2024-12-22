package ru.yandex.practicum.filmorate.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {
    public static Film filmDtoToFilm(FilmDto dto) {
        Film.FilmBuilder builder = Film.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration());

        if (dto.getGenres() != null) {
            builder.genres(dto.getGenres().stream()
                    .map(FilmGenre::getId)
                    .collect(Collectors.toSet())
            );
        }

        if (dto.getMpa() != null) {
            builder.mpaRating(dto.getMpa().getId());
        }

        return builder.build();
    }

    public static FilmDto filmToFilmDto(Film film, GenreStorage genreStorage, MpaStorage mpaStorage) {
        FilmDto.FilmDtoBuilder builder = FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration());

        if (film.getMpaRating() != null) {
            builder.mpa(mpaStorage.getById(film.getMpaRating()));
        }

        if (film.getGenres() != null) {
            builder.genres(film.getGenres().stream()
                    .map(genreStorage::getById)
                    .sorted((g1, g2) -> g1.getId() - g2.getId())
                    .collect(Collectors.toCollection(LinkedHashSet::new))
            );
        }

        return builder.build();
    }
}
