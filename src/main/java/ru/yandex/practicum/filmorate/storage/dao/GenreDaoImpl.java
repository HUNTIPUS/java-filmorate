package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.characteristicsForFilm.Genre;
import ru.yandex.practicum.filmorate.storage.dal.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class GenreDaoImpl implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Genre> getGenreById(Integer genreId) {
        String sql = "select * from GENRES where GENRE_ID = ?";
        final List<Genre> genres = jdbcTemplate.query(sql, GenreDaoImpl::makeGenre, genreId);

        if (genres.size() != 1) {
            log.info("Жанр с id = {} не найден", genreId);
            return Optional.empty();
        }
        log.info("Жанр с id = {} найден", genreId);
        return Optional.of(genres.get(0));
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("select * from GENRES", GenreDaoImpl::makeGenre);
    }

    private static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("genre_id"),
                rs.getString("genre_name"));
    }
}
