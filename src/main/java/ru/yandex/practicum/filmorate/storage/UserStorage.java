package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

public interface UserStorage {
    User createUser(User user);
    User updateUser(User user);
    List<User> getUsers();

    User getUserById(Integer userId);
}
