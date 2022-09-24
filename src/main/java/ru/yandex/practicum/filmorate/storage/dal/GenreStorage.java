package ru.yandex.practicum.filmorate.storage.dal;

import ru.yandex.practicum.filmorate.characteristicsForFilm.Genre;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    Optional<Genre> getGenreById(Integer genreId);

    List<Genre> getAllGenres();

    void outputGenres(Film film);
}
