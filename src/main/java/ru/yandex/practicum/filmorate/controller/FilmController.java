package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ObjectExcistenceException;
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
@Validated
public class FilmController {
    private final FilmService filmService;
    private final LocalDate START_DATA_FILM = LocalDate.of(1895, 12, 28);

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        if (doValidation(film.getReleaseDate())) {
            log.info("Добавление фильма");
            return filmService.createFilm(film);
        }
        return null;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        if (doValidate(film.getId())) {
            if (doValidation(film.getReleaseDate())) {
                log.info("Обновление данных о фильме");
                return filmService.updateFilm(film);
            }
        }
        return null;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Показ всех фильмов");
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Integer filmId) {
        log.info("Показ фильма по его id");
        return filmService.getFilmById(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable("userId") Integer userId,
                              @PathVariable("id") Integer filmId) {
        if (doValidate(userId)) {
            log.info("Пользователь с id = " + userId + " поставил лайк фильму с id = " + filmId);
            filmService.addLikeToFilm(userId, filmId);
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeToFilm(@PathVariable("userId") Integer userId,
                                 @PathVariable("id") Integer filmId) {
        if (doValidate(userId)) {
            log.info("Пользователь с id = " + userId + " убрал лайк с фильма с id = " + filmId);
            filmService.deleteLikeToFilm(userId, filmId);
        }
    }

    @GetMapping("/popular")
    public List<Film> getTopTenFilms(@Positive @RequestParam(required = false, defaultValue = "10") Integer count) {
        log.info("Топ " + count + " лучших фильмов");
        return filmService.getTopTenFilms(count);
    }

    private Boolean doValidation(LocalDate dateFilm) {
        if (!dateFilm.isBefore(START_DATA_FILM)) {
            return true;
        } else {
            throw new ValidationException("Дата меньше 28.12.1895");
        }
    }

    private Boolean doValidate(Integer userId) {
        if (userId != null && userId > 0) {
            return true;
        }
        throw new ObjectExcistenceException("Ошибка существования объекта");
    }
}
