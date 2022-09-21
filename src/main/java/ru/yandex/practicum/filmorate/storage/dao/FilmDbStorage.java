package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.characteristicsForFilm.Genre;
import ru.yandex.practicum.filmorate.characteristicsForFilm.Mpa;
import ru.yandex.practicum.filmorate.exception.ObjectExcistenceException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dal.FilmStorage;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film createFilm(Film film) {

        String sql =
                "insert into FILMS (TITLE, DESCRIPTION, RELEASE_DATE, DURATION, MOTION_PICTURE_ASSOCIATION_ID) " +
                        "values(?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());

        String sqlForFilm = "select count(*) from FILMS";
        film.setId(jdbcTemplate.queryForObject(sqlForFilm, Integer.class));

        insertFilmGenre(film);

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql =
                "update FILMS set TITLE = ?," +
                        " DESCRIPTION = ?," +
                        " RELEASE_DATE = ?," +
                        " DURATION = ?," +
                        " MOTION_PICTURE_ASSOCIATION_ID = ?" +
                        " where FILM_ID = ?";

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (getGenreFilmsByFilmId(film.getId())) {
            String sqlForUpdateGenre = "delete from FILM_GENRE where FILM_ID = ?";
            jdbcTemplate.update(sqlForUpdateGenre, film.getId());
            insertFilmGenre(film);
        } else {
            insertFilmGenre(film);
        }

        return getFilmById(film.getId()).orElseThrow(() -> new ObjectExcistenceException("Фильм не найден"));
    }

    @Override
    public List<Film> getFilms() {
        String sqlForFilms =
                "select * from FILMS F join MOTION_PICTURE_ASSOCIATION M on F.MOTION_PICTURE_ASSOCIATION_ID = M.MPA_ID";

        List<Film> films = jdbcTemplate.query(sqlForFilms, (rs, rowNum) ->
                new Film(rs.getInt("film_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("release_date").toLocalDate(),
                        rs.getInt("duration"),
                        new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name"))));


        for (Film film: films) {
            addLikesAndGenres(film);
        }

        return films;
    }

    @Override
    public Optional<Film> getFilmById(Integer filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * " +
                "from FILMS F " +
                "join MOTION_PICTURE_ASSOCIATION M on F.MOTION_PICTURE_ASSOCIATION_ID = M.MPA_ID " +
                "where film_id = ?", filmId);

        if (filmRows.next()) {
            Film film = new Film(
                    filmRows.getInt("film_id"),
                    filmRows.getString("title"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"),
                    new Mpa(filmRows.getInt("motion_picture_association_id"),
                            filmRows.getString("mpa_name")));
            addLikesAndGenres(film);
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", filmId);
            return Optional.empty();
        }
    }

    public void addLikeToFilm(Integer userId, Integer filmId) {
        String sql =
                "insert into FILM_USER (film_id, user_id)" +
                        "values (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public List<Integer> getUsersWhichLikeFilm(Integer filmId) {
        String sql =
                "select FU.USER_ID " +
                "from FILMS F " +
                "join FILM_USER FU on F.FILM_ID = FU.FILM_ID " +
                "where F.FILM_ID = " + filmId;
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("user_id"));
    }

    public void deleteLikeToFilm(Integer userId, Integer filmId) {
        String sql =
                "delete from FILM_USER where FILM_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public List<Film> getTopTenFilms(Integer numberForTop) {
        String sql =
                "select F.FILM_ID," +
                        "       F.TITLE," +
                        "       F.DESCRIPTION," +
                        "       F.RELEASE_DATE," +
                        "       F.DURATION," +
                        "       M.MPA_ID," +
                        "       M.MPA_NAME,"+
                        "       count(FU.USER_ID) " +
                        "from FILMS F " +
                        "left join MOTION_PICTURE_ASSOCIATION M on F.MOTION_PICTURE_ASSOCIATION_ID = M.MPA_ID " +
                        "left join FILM_USER FU on F.FILM_ID = FU.FILM_ID " +
                        "group by F.FILM_ID " +
                        "order by count(FU.USER_ID) desc " +
                        "limit " + numberForTop;

        List<Film> films =  jdbcTemplate.query(sql, (rs, rowNum) ->
                new Film(rs.getInt("film_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("release_date").toLocalDate(),
                        rs.getInt("duration"),
                        new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name"))));

        for (Film film: films) {
            addLikesAndGenres(film);
        }

        return films;
    }

    private void insertFilmGenre(Film film) {
        String sqlForGenre = "insert into FILM_GENRE(film_id, genre_id) values (?, ?)";

        for (Genre genre : film.getGenres()) {
            if (!catchDuplicates(film.getId(), genre.getId())) {
                jdbcTemplate.update(sqlForGenre,
                        film.getId(),
                        genre.getId());
            }
        }
    }

    private Boolean catchDuplicates (Integer filmId, Integer genreId) {
        String sql = "select FILM_ID from FILM_GENRE where FILM_ID = "+ filmId + " and GENRE_ID = " + genreId;

        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("film_id")).contains(filmId);
    }

    private Boolean getGenreFilmsByFilmId(Integer filmId) {
        String sql = "select FILM_ID from FILM_GENRE where FILM_ID = " + filmId;
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("film_id")).contains(filmId);
    }

    private Film addLikesAndGenres(Film film) {

        String sqlForLikes1 = "select count(*) " +
                "from film_user fu " +
                "join users u on u.user_id = fu.user_id " +
                "where film_id = " + film.getId();
        String sqlForLikes2 = "select * " +
                "from film_user fu " +
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
        film.setUsersWhichLikeFilm(jdbcTemplate.query(sqlForLikes2,
                (rs, rowNum) -> new User(
                        rs.getInt("user_id"),
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("user_name"),
                        rs.getDate("birthday").toLocalDate()
                )));
        return film;
    }
}
