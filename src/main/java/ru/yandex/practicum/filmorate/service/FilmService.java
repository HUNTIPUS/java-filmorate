package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final InMemoryFilmStorage filmStorage;

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
        return filmStorage.getFilmById(filmId);
    }

    public void addLikeToFilm(Integer userId, Integer filmId) {
        List<Film> films = getFilms();
        Film film = findFilmById(filmId, films);
        if (!film.getUsersWhichLikeFilm().contains(userId)) {
            film.addLikes();
            film.addUsersWhichLikeFilm(userId);
        } else {
            throw new UnsupportedOperationException(String.format("Пользователь № %d уже поставил лайк", userId));
        }
    }

    public void deleteLikeToFilm(Integer userId, Integer filmId) {
        List<Film> films = getFilms();
        Film film = findFilmById(filmId, films);
        if (film.getUsersWhichLikeFilm().contains(userId)) {
            film.deleteLikes();
            film.deleteLikeByUser(userId);
        } else {
            throw new UnsupportedOperationException(String.format("Пользователь № %d уже убрал лайк", userId));
        }
    }

    public List<Film> getTopTenFilms(Integer numberForTop) {
        List<Film> films = getFilms();
        Collections.sort(films, (o1, o2) -> o2.getCountLikes() - o1.getCountLikes());
        if (films.size() < numberForTop) {
            return films;
        }
        return films.subList(0, numberForTop);
    }

    private Film findFilmById(Integer idFilm, List<Film> films) {
        return films.stream()
                .filter(x -> x.getId().equals(idFilm))
                .findFirst()
                .orElseThrow(() -> new ValidationException(String.format("Фильм № %d не найден", idFilm)));
    }
}
