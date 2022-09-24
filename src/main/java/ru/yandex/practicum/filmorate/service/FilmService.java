package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.characteristicsForFilm.Genre;
import ru.yandex.practicum.filmorate.exception.ObjectExcistenceException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dal.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dal.LikeStorage;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmDbStorage;
    private final LikeStorage likeDao;
    private final JdbcTemplate jdbcTemplate;
    public Film createFilm(Film film) {
        Film filmNew = filmDbStorage.createFilm(film);
        outputLikesAndGenres(filmNew);
        return filmNew;
    }

    public Film updateFilm(Film film) {
        Film filmNew = filmDbStorage.updateFilm(film);
        outputLikesAndGenres(filmNew);
        return filmNew;
    }

    public List<Film> getFilms() {
        List<Film> films = filmDbStorage.getFilms();
        for (Film film: films) {
            outputLikesAndGenres(film);
        }
        return films;
    }

    public Film getFilmById(Integer filmId) {
        Film film = filmDbStorage.getFilmById(filmId)
                .orElseThrow(() -> new ObjectExcistenceException("Фильм не существует"));
        outputLikesAndGenres(film);
        return film;
    }

    public void addLikeToFilm(Integer userId, Integer filmId) {
        getFilmById(filmId);
        if (!likeDao.getUsersWhichLikeFilm(filmId).contains(userId)) {
            likeDao.addLikeToFilm(userId, filmId);
        } else {
            throw new ValidationException(String.format("Пользователь № %d уже поставил лайк", userId));
        }
    }

    public void deleteLikeToFilm(Integer userId, Integer filmId) {
        getFilmById(filmId);
        if (likeDao.getUsersWhichLikeFilm(filmId).contains(userId)) {
            likeDao.deleteLikeToFilm(userId, filmId);
        } else {
            throw new ValidationException(String.format("Пользователь № %d уже убрал лайк", userId));
        }
    }

    public List<Film> getTopTenFilms(Integer numberForTop) {
        return filmDbStorage.getTopTenFilms(numberForTop);
    }

    private void outputLikesAndGenres(Film film) {
        String sqlForLikes1 = "select count(*) " +
                "from likes fu " +
                "join users u on u.user_id = fu.user_id " +
                "where film_id = " + film.getId();

        String sqlForGenre =
                "select G2.GENRE_ID, " +
                        "       G2.GENRE_NAME " +
                        "from FILMS F " +
                        "left join MOTION_PICTURE_ASSOCIATION MPA on MPA.MPA_ID = F.MOTION_PICTURE_ASSOCIATION_ID " +
                        "left join FILM_GENRE FG on F.FILM_ID = FG.FILM_ID " +
                        "left join GENRES G2 on G2.GENRE_ID = FG.GENRE_ID " +
                        "where F.FILM_ID = " + film.getId();

        List<Genre> genres = jdbcTemplate.query(sqlForGenre, (rs, rowNun) -> new Genre(
                rs.getInt("genre_id"),
                rs.getString("genre_name")
        ));
        if (!genres.get(0).getId().equals(0) && !genres.isEmpty()) {
            film.setGenres(genres);
        }
        film.setCountLikes(jdbcTemplate.queryForObject(sqlForLikes1, Integer.class));
    }
}
