package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.characteristicsForFilm.Mpa;
import ru.yandex.practicum.filmorate.storage.dal.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MpaDaoImpl implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Mpa> getMpaById(Integer mpaId) {
        String sql = "select * from MOTION_PICTURE_ASSOCIATION where MPA_ID = ?";

        final List<Mpa> mpa = jdbcTemplate.query(sql, MpaDaoImpl::makeMpa, mpaId);

        if (mpa.size() != 1) {
            log.info("Возрастное ограничение с id = {} не найдено", mpaId);
            return Optional.empty();
        }
        log.info("Возрастное ограничение с id = {} найдено", mpaId);
        return Optional.of(mpa.get(0));

    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "select * from MOTION_PICTURE_ASSOCIATION";
        return jdbcTemplate.query(sql, MpaDaoImpl::makeMpa);
    }

    private static Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(
                rs.getInt("mpa_id"),
                rs.getString("mpa_name")
        );
    }
}
