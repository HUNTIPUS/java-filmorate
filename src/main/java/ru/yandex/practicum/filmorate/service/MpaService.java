package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.characteristicsForFilm.Mpa;
import ru.yandex.practicum.filmorate.exception.ObjectExcistenceException;
import ru.yandex.practicum.filmorate.storage.dal.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaDao;

    public Mpa getMpaById(Integer mpaId) {
        return mpaDao.getMpaById(mpaId)
                .orElseThrow(() -> new ObjectExcistenceException("Такого возрастного ограничения не существует"));
    }

    public List<Mpa> getAllMpa(){
        return mpaDao.getAllMpa();
    }
}
