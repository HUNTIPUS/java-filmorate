package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final InMemoryFilmStorage filmStorage;
    private final FilmService filmService;

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        return filmStorage.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        return filmStorage.updateFilm(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Integer filmId) {
        return filmStorage.getFilmById(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable("userId") Integer userId,
                              @PathVariable("id") Integer idFilm) {
        filmService.addLikeToFilm(userId, idFilm, filmStorage.getFilms());
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeToFilm(@PathVariable("userId") Integer userId,
                                 @PathVariable("id") Integer idFilm) {
        filmService.deleteLikeToFilm(userId, idFilm, filmStorage.getFilms());
    }

    @GetMapping("/popular")
    public List<Film> getTopTenFilmsWithoutParametrs(@RequestParam(required = false) Integer count) {
        return filmService.getTopTenFilms(filmStorage.getFilms(), count);
    }
}
