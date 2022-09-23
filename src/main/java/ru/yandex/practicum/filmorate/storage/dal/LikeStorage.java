package ru.yandex.practicum.filmorate.storage.dal;

import java.util.List;

public interface LikeStorage {

    void addLikeToFilm(Integer userId, Integer filmId);

    List<Integer> getUsersWhichLikeFilm(Integer filmId);

    void deleteLikeToFilm(Integer userId, Integer filmId);
}
