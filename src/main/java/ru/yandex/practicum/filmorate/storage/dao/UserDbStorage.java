package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dal.UserStorage;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    @Override
    public User createUser(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        String sql =
        "insert into USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) " +
                "values (?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        String sqlForUser = "select count(*) from USERS";
        user.setId(jdbcTemplate.queryForObject(sqlForUser, Integer.class));

        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql =
                "update USERS set EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? where USER_ID = ?";

        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        return user;
    }

    @Override
    public List<User> getUsers() {
        String sql =
                "select * " +
                "from USERS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("user_name"),
                rs.getDate("birthday").toLocalDate()
        ));
    }

    @Override
    public Optional<User> getUserById(Integer userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where user_id = ?", userId);

        if(userRows.next()) {
            User user = new User(
                    userRows.getInt("user_id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("user_name"),
                    Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate());

            log.info("Найден пользователь: {} {}", user.getId(), user.getName());

            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", userId);
            return Optional.empty();
        }
    }

    public void addFriend(Integer userId, Integer friendId, Integer statusId) {
        String sql =
                "insert into FRIENDS (FIRST_USER_ID, SECOND_USER_ID, STATUS_ID) " +
                "values (?, ?, ?)";

        jdbcTemplate.update(sql, userId, friendId, statusId);
    }

    public void updateStatusFriendship (Integer userId, Integer friendId, Integer statusId) {
        String sql =
                "update FRIENDS set STATUS_ID = ? where " +
                        "FIRST_USER_ID = ? and SECOND_USER_ID = ?";
        jdbcTemplate.update(sql, statusId, userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        String sql =
                "delete from FRIENDS where FIRST_USER_ID = ? and SECOND_USER_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public List<User> getFriends(Integer userId) {
        String sql =
                "select U.USER_ID," +
                "       U.EMAIL," +
                "       U.LOGIN," +
                "       U.USER_NAME," +
                "       U.BIRTHDAY " +
                "from USERS " +
                "join FRIENDS F on USERS.USER_ID = F.FIRST_USER_ID " +
                "join USERS U on F.SECOND_USER_ID = U.USER_ID " +
                "where F.STATUS_ID = 2 and F.FIRST_USER_ID = " + userId;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("user_name"),
                rs.getDate("birthday").toLocalDate()
        ));
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        String sql =
                "select U.USER_ID," +
                        "       U.EMAIL," +
                        "       U.LOGIN," +
                        "       U.USER_NAME," +
                        "       U.BIRTHDAY " +
                        "from FRIENDS F2 " +
                        "inner join USERS U on U.USER_ID = F2.SECOND_USER_ID " +
                        "where F2.STATUS_ID = 2 " +
                        "and F2.FIRST_USER_ID = " + userId +
                        " and F2.SECOND_USER_ID IN ( " +
                        "    select F1.SECOND_USER_ID " +
                        "    from FRIENDS F1 " +
                        "    where F1.STATUS_ID = 2 and F1.FIRST_USER_ID = " + friendId + ")";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("user_name"),
                rs.getDate("birthday").toLocalDate()
        ));
    }
}
