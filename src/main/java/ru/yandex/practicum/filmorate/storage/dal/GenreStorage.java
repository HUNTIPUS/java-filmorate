package ru.yandex.practicum.filmorate.storage.dal;

import ru.yandex.practicum.filmorate.characteristicsForFilm.Genre;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    Optional<Genre> getGenreById(Integer genreId) throws SQLException;

    List<Genre> getAllGenres();
}
