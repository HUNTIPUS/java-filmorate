package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.generate.GenerateId;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final LocalDate START_DATA_FILM = LocalDate.of(1895, 12, 28);
    private final GenerateId generateId;

    @Override
    public Film createFilm(Film film) {
        if (doValidation(film.getReleaseDate())) {
            film.setId(generateId.getId());
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("Не удалось добавить фильм");
        }
    }

    @Override
    public Film updateFilm(Film film) {
        if (doValidation(film.getReleaseDate()) && film.getId() > 0) {
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
            }
            return film;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с таким id не существует");
        }
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Integer filmId) {
        if (filmId > 0) {
            return films.get(filmId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с таким id не существует");
        }
    }

        private Boolean doValidation (LocalDate dateFilm){
            return !dateFilm.isBefore(START_DATA_FILM);
        }
    }
