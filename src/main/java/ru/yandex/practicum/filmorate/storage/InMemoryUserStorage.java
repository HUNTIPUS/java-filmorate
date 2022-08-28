package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.generate.GenerateId;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage{

    private final Map<Integer, User> users = new HashMap<>();
    private final GenerateId generateId;

    @Override
    public User createUser(User user) {
        if (doValidate(user)) {
            user.setId(generateId.getId());
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("Не удалось создать пользователя");
        }
    }

    @Override
    public User updateUser(User user) {
        if (doValidate(user) && user.getId() > 0) {
            if(users.containsKey(user.getId())) {
                users.put(user.getId(), user);
            }
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с таким id не существует");
        }
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Integer userId) {
        if (userId > 0) {
            return users.get(userId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с таким id не существует");
        }
    }

    private Boolean doValidate(User user) {
        if (!user.getLogin().contains(" ")) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            return true;
        }
        return false;
    }
}
