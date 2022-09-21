package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.characteristicsForFilm.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
@Validated
public class MpaController {
    private final MpaService mpaService;

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable("id") Integer mpaId) {
        return mpaService.getMpaById(mpaId);
    }

    @GetMapping
    public List<Mpa> getAllMpa() {
        return mpaService.getAllMpa();
    }

}
