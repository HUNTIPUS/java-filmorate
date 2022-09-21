package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.characteristicsForFilm.Mpa;
import ru.yandex.practicum.filmorate.storage.dal.MpaStorage;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MpaDaoImpl implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Mpa> getMpaById(Integer mpaId) {
        SqlRowSet mpaRows = jdbcTemplate
                .queryForRowSet("select * from MOTION_PICTURE_ASSOCIATION where MPA_ID = ?", mpaId);

        if (mpaRows.next()) {
            Mpa mpa = new Mpa(
                    mpaRows.getInt("mpa_id"),
                    mpaRows.getString("mpa_name")
            );

            log.info("Найдено возрастное ограничение: {} {}", mpa.getId(), mpa.getName());
            return Optional.of(mpa);
        } else {
            log.info("Возрастное ограничение с идентификатором {} не найдено.", mpaId);
            return Optional.empty();
        }

    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "select * from MOTION_PICTURE_ASSOCIATION";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Mpa(
                rs.getInt("mpa_id"),
                rs.getString("mpa_name")
        ));
    }
}
