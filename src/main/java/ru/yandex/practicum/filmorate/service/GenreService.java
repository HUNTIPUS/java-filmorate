package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.characteristicsForFilm.Genre;
import ru.yandex.practicum.filmorate.exception.ObjectExcistenceException;
import ru.yandex.practicum.filmorate.storage.dal.GenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreDao;

    public Genre getGenreById(Integer genreId) {
        return genreDao.getGenreById(genreId)
                .orElseThrow(() -> new ObjectExcistenceException("Жанра с таким id не существует"));
    }

    public List<Genre> getAllMpa() {
        return genreDao.getAllGenres();
    }
}
