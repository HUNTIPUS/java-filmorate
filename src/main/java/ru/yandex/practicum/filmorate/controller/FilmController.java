package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.generate.GenerateId;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private final LocalDate START_DATA_FILM = LocalDate.of(1895, 12, 28);
    private final GenerateId generateId;

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        if (doValidation(film.getReleaseDate())) {
            film.setId(generateId.getId());
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("Не удалось добавить фильм");
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        if (doValidation(film.getReleaseDate()) && film.getId() > 0) {
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
            }
            return film;
        } else {
            throw new ValidationException("Не удалось обновить фильм");
        }
    }

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    private Boolean doValidation(LocalDate dateFilm) {
        return !dateFilm.isBefore(START_DATA_FILM);
    }
}
