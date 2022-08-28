package ru.yandex.practicum.filmorate.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {

    private static final Comparator<Film> COMPARATOR_LIKES = Comparator.comparing(Film::getCountLikes);

    public void addLikeToFilm(Integer userId, Integer filmId, List<Film> films) {
        if (doValidate(userId, filmId)) {
            Film film = findFilmById(filmId, films);
            if (!film.getUsersWhichLikeFilm().contains(userId)) {
                film.addLikes();
                film.addUsersWhichLikeFilm(userId);
            } else {
                throw new ValidationException(String.format("Пользователь № %d уже поставил лайк", userId));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм или/и пользователь с таким" +
                    " id не существует/ют");
        }
    }

    public void deleteLikeToFilm(Integer userId, Integer filmId, List<Film> films) {
        if (doValidate(userId, filmId)) {
            Film film = findFilmById(filmId, films);
            if (film.getUsersWhichLikeFilm().contains(userId)) {
                film.deleteLikes();
                film.deleteLikeByUser(userId);
            } else {
                throw new ValidationException(String.format("Пользователь № %d уже убрал лайк", userId));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм или/и пользователь с таким" +
                    " id не существует/ют");
        }
    }

    public List<Film> getTopTenFilms(List<Film> films, Integer numberForTop) {
        if (numberForTop == null || numberForTop.equals(0)) {
            numberForTop = 10;
        }
        Collections.sort(films, (o1, o2) -> o2.getId() - o1.getId());
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

    private Boolean doValidate(Integer userId, Integer filmId) {
        if (userId != null && userId > 0
                && filmId != null && filmId > 0) {
            return true;
        }
        return false;
    }
}
