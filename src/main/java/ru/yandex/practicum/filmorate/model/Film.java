package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.time.Instant;

@Data
@AllArgsConstructor
public class Film {
    private Long id;
    private String name;
    private String description;
    private Instant releaseDate;
    private Duration duration;
}
