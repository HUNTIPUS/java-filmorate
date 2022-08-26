package ru.yandex.practicum.filmorate.controller;


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

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private GenerateId generateId = new GenerateId();

    @PostMapping
    public Film createFilm(@RequestBody @Valid @NotNull Film film) {
        if (doValidation(film.getReleaseDate())) {
            film.setId(generateId.getId());
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("Не удалось добавить фильм");
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid @NotNull Film film) {
        if (doValidation(film.getReleaseDate()) && film.getId() > 0) {
            for (Integer id : films.keySet()) {
                if (id == film.getId()) {
                    films.put(film.getId(), film);
                }
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
        return !dateFilm.isBefore(LocalDate.of(1895, 12, 28));
    }
}
