package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.generate.GenerateIdFilm;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private GenerateIdFilm generateIdFilm = new GenerateIdFilm();

    @PostMapping
    public Film createFilm(@RequestBody @NotNull Film film) {
        if (!film.getName().isEmpty()
                && film.getDescription().length() < 201
                && film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))
                && film.getDuration() > 0) {

            film.setId(generateIdFilm.getId());
            System.out.println(film.getName());
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("Не удалось добавить фильм");
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody @NotNull Film film) {
        if (!film.getName().isEmpty()
                && film.getDescription().length() < 201
                && film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))
                && film.getDuration() > 0 && film.getId() > 0) {

            for (Integer id: films.keySet()) {
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
    public ArrayList<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
}
