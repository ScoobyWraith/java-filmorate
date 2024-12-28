package ru.yandex.practicum.filmorate.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {
    public static Film filmDtoToFilm(FilmDto dto) {
        Film.FilmBuilder builder = Film.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .mpaRating(dto.getMpa())
                .genres(dto.getGenres())
                .duration(dto.getDuration());

        return builder.build();
    }

    public static FilmDto filmToFilmDto(Film film) {
        return FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .mpa(film.getMpaRating())
                .genres(film.getGenres())
                .duration(film.getDuration())
                .build();
    }
}
