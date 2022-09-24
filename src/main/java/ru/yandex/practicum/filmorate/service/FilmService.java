package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectExcistenceException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dal.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dal.GenreStorage;
import ru.yandex.practicum.filmorate.storage.dal.LikeStorage;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmDbStorage;
    private final LikeStorage likeDao;
    private final GenreStorage genreDao;
    public Film createFilm(Film film) {
        Film filmNew = filmDbStorage.createFilm(film);
        genreDao.outputGenres(filmNew);
        return filmNew;
    }

    public Film updateFilm(Film film) {
        Film filmNew = filmDbStorage.updateFilm(film);
        genreDao.outputGenres(filmNew);
        return filmNew;
    }

    public List<Film> getFilms() {
        List<Film> films = filmDbStorage.getFilms();
        for (Film film: films) {
            genreDao.outputGenres(film);
        }
        return films;
    }

    public Film getFilmById(Integer filmId) {
        Film film = filmDbStorage.getFilmById(filmId)
                .orElseThrow(() -> new ObjectExcistenceException("Фильм не существует"));
        genreDao.outputGenres(film);
        return film;
    }

    public void addLikeToFilm(Integer userId, Integer filmId) {
        getFilmById(filmId);
        if (!likeDao.getUsersWhichLikeFilm(filmId).contains(userId)) {
            likeDao.addLikeToFilm(userId, filmId);
        } else {
            throw new ValidationException(String.format("Пользователь № %d уже поставил лайк", userId));
        }
    }

    public void deleteLikeToFilm(Integer userId, Integer filmId) {
        getFilmById(filmId);
        if (likeDao.getUsersWhichLikeFilm(filmId).contains(userId)) {
            likeDao.deleteLikeToFilm(userId, filmId);
        } else {
            throw new ValidationException(String.format("Пользователь № %d уже убрал лайк", userId));
        }
    }

    public List<Film> getTopTenFilms(Integer numberForTop) {
        return filmDbStorage.getTopTenFilms(numberForTop);
    }
}
