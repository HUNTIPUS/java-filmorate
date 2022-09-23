package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectExcistenceException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.LikeDaoImpl;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDbStorage filmDbStorage;
    private final LikeDaoImpl likeDao;
    public Film createFilm(Film film) {
        Film filmNew = filmDbStorage.createFilm(film);
        filmDbStorage.outputLikesAndGenres(filmNew);
        return filmNew;
    }

    public Film updateFilm(Film film) {
        Film filmNew = filmDbStorage.updateFilm(film);
        filmDbStorage.outputLikesAndGenres(filmNew);
        return filmNew;
    }

    public List<Film> getFilms() {
        List<Film> films = filmDbStorage.getFilms();
        for (Film film: films) {
            filmDbStorage.outputLikesAndGenres(film);
        }
        return films;
    }

    public Film getFilmById(Integer filmId) {
        Film film = filmDbStorage.getFilmById(filmId)
                .orElseThrow(() -> new ObjectExcistenceException("Фильм не существует"));
        filmDbStorage.outputLikesAndGenres(film);
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
