package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.dal.LikeStorage;

import java.util.List;


@Repository
@RequiredArgsConstructor
@Slf4j
public class LikeDaoImpl implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLikeToFilm(Integer userId, Integer filmId) {
        String sql =
                "insert into LIKES (film_id, user_id)" +
                        "values (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
        updateRate(filmId);
    }

    @Override
    public List<Integer> getUsersWhichLikeFilm(Integer filmId) {
        String sql =
                "select L.USER_ID " +
                        "from FILMS F " +
                        "join LIKES L on F.FILM_ID = L.FILM_ID " +
                        "where F.FILM_ID = ?";

        log.info("Просмотр всех id пользователей, которые лайкнули фильм с id = {}", filmId);
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("user_id"), filmId);
    }

    @Override
    public void deleteLikeToFilm(Integer userId, Integer filmId) {
        String sql =
                "delete from LIKES where FILM_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);
        updateRate(filmId);
    }

    private void updateRate(long filmId) {
        String sqlQuery = "update FILMS f set rate = " +
                "(select count(l.user_id) from LIKES l where l.film_id = f.film_id)  where film_id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }
}
