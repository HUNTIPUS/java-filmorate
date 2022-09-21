package ru.yandex.practicum.filmorate.storage.dal;

import ru.yandex.practicum.filmorate.characteristicsForFilm.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    Optional<Genre> getGenreById(Integer genreId);

    List<Genre> getAllGenres();
}
