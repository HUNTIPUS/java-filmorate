package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dal.FriendStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class FriendDaoImpl implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        String sql =
                "insert into FRIENDS (FIRST_USER_ID, SECOND_USER_ID) " +
                        "values (?, ?)";

        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        String sql =
                "delete from FRIENDS where FIRST_USER_ID = ? and SECOND_USER_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
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
                        "where F.FIRST_USER_ID = ?";

        return jdbcTemplate.query(sql, FriendDaoImpl::makeUser, userId);
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        String sql =
                "select U.USER_ID," +
                        "       U.EMAIL," +
                        "       U.LOGIN," +
                        "       U.USER_NAME," +
                        "       U.BIRTHDAY " +
                        "from FRIENDS F2 " +
                        "inner join USERS U on U.USER_ID = F2.SECOND_USER_ID " +
                        "where F2.FIRST_USER_ID = ?" +
                        " and F2.SECOND_USER_ID IN ( " +
                        "    select F1.SECOND_USER_ID " +
                        "    from FRIENDS F1 " +
                        "    where F1.FIRST_USER_ID = ?)";

        return jdbcTemplate.query(sql, FriendDaoImpl::makeUser, userId, friendId);
    }

    private static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("user_name"),
                rs.getDate("birthday").toLocalDate()
        );
    }
}
