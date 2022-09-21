package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.characteristicsForFilm.Genre;
import ru.yandex.practicum.filmorate.storage.dal.GenreStorage;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenreDaoImpl implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Genre> getGenreById(Integer genreId) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from GENRES where GENRE_ID = ?", genreId);

        if(genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getInt("genre_id"),
                    genreRows.getString("genre_name")
            );

            log.info("Найден жанр: {} {}", genre.getId(), genre.getName());
            return Optional.of(genre);
        } else {
            log.info("Жанр с идентификатором {} не найден.", genreId);
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql =
                "select * from GENRES";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(
                rs.getInt("genre_id"),
                rs.getString("genre_name")
        ));
    }
}
