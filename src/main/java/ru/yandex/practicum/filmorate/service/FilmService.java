package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectExcistenceException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Integer filmId) {
        return filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new ObjectExcistenceException("Фильм не существует"));
    }

    public void addLikeToFilm(Integer userId, Integer filmId) {
        Film film = getFilmById(filmId);
        if (!film.getUsersWhichLikeFilm().contains(userId)) {
            film.addLikes(userId);
        } else {
            throw new ValidationException(String.format("Пользователь № %d уже поставил лайк", userId));
        }
    }

    public void deleteLikeToFilm(Integer userId, Integer filmId) {
        Film film = getFilmById(filmId);
        if (film.getUsersWhichLikeFilm().contains(userId)) {
            film.deleteLikes(userId);
        } else {
            throw new ValidationException(String.format("Пользователь № %d уже убрал лайк", userId));
        }
    }

    public List<Film> getTopTenFilms(Integer numberForTop) {
        return getFilms().stream()
                .sorted(Comparator.comparingInt(Film::getCountLikes).reversed())
                .limit(numberForTop)
                .collect(Collectors.toList());
    }
}
