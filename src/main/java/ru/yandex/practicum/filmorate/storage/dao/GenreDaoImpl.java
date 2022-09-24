package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.characteristicsForFilm.Genre;
import ru.yandex.practicum.filmorate.model.Film;
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

    @Override
    public void outputGenres(Film film) {
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
    }
}
