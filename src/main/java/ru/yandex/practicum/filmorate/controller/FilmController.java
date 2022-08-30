package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
@Validated
public class FilmController {
    private final FilmService filmService;
    private final LocalDate START_DATA_FILM = LocalDate.of(1895, 12, 28);

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        if (doValidation(film.getReleaseDate())) {
            return filmService.createFilm(film);
        } else {
            throw new ValidationException("Не удалось добавить фильм");
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        if (film.getId() > 0 && film.getId() != null) {
            if (doValidation(film.getReleaseDate())) {
                return filmService.updateFilm(film);
            } else {
                throw new ValidationException("Дата меньше 28.12.1895");
            }
        } else {
            throw new NullPointerException("Фильм с таким id не существует");
        }
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Integer filmId) {
        if (filmId > 0) {
            return filmService.getFilmById(filmId);
        } else {
            throw new NullPointerException("Фильм с таким id не существует");
        }
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable("userId") Integer userId,
                              @PathVariable("id") Integer filmId) {
        if (doValidate(userId, filmId)) {
            filmService.addLikeToFilm(userId, filmId);
        } else {
            throw new NullPointerException("Фильм или/и пользователь с таким id не существует/ют");
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeToFilm(@PathVariable("userId") Integer userId,
                                 @PathVariable("id") Integer filmId) {
        if (doValidate(userId, filmId)) {
            filmService.deleteLikeToFilm(userId, filmId);
        } else {
            throw new NullPointerException("Фильм или/и пользователь с таким id не существует/ют");
        }
    }

    @GetMapping("/popular")
    public List<Film> getTopTenFilms(@Positive @RequestParam(required = false, defaultValue = "10") Integer count) {
        return filmService.getTopTenFilms(count);
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<String> exc(ValidationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<String> exc(NullPointerException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UnsupportedOperationException .class)
    public ResponseEntity<String> exc(UnsupportedOperationException  ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Boolean doValidation(LocalDate dateFilm) {
        return !dateFilm.isBefore(START_DATA_FILM);
    }

    private Boolean doValidate(Integer userId, Integer filmId) {
        if (userId != null && userId > 0
                && filmId != null && filmId > 0) {
            return true;
        }
        return false;
    }
}
