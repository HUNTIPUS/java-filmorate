package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.characteristicsForFilm.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {

    Optional<Mpa> getMpaById(Integer mpaId);

    List<Mpa> getAllMpa();
}
