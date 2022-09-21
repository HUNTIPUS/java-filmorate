package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectExcistenceException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDbStorage filmDbStorage;
    public Film createFilm(Film film) {
        return filmDbStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmDbStorage.updateFilm(film);
    }

    public List<Film> getFilms() {
        return filmDbStorage.getFilms();
    }

    public Film getFilmById(Integer filmId) {
        return filmDbStorage.getFilmById(filmId)
                .orElseThrow(() -> new ObjectExcistenceException("Фильм не существует"));
    }

    public void addLikeToFilm(Integer userId, Integer filmId) {
        getFilmById(filmId);
        if (!filmDbStorage.getUsersWhichLikeFilm(filmId).contains(userId)) {
            filmDbStorage.addLikeToFilm(userId, filmId);
        } else {
            throw new ValidationException(String.format("Пользователь № %d уже поставил лайк", userId));
        }
    }

    public void deleteLikeToFilm(Integer userId, Integer filmId) {
        getFilmById(filmId);
        if (filmDbStorage.getUsersWhichLikeFilm(filmId).contains(userId)) {
            filmDbStorage.deleteLikeToFilm(userId, filmId);
        } else {
            throw new ValidationException(String.format("Пользователь № %d уже убрал лайк", userId));
        }
    }

    public List<Film> getTopTenFilms(Integer numberForTop) {
        return filmDbStorage.getTopTenFilms(numberForTop);
    }
}
