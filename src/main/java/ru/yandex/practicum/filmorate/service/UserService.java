package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectExcistenceException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User createUser(User user) {
        return userStorage.createUser(user);
    }


    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }


    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(Integer userId) {
        return userStorage.getUserById(userId)
                .orElseThrow(() -> new ObjectExcistenceException("Пользователь не существует"));
    }

    public void addFriend(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        if (!user.getFriends().contains(friend.getId())) {
            user.addFriends(friendId);
            friend.addFriends(userId);
        } else {
            throw new ValidationException(String.format("Пользователь № %d уже у вас в друзьях", userId));
        }
    }

    public void deleteFriend(Integer userId, Integer friendId) {
            User user = getUserById(userId);
            User friend = getUserById(friendId);

            if (user.getFriends().contains(friendId)) {
                user.deleteFriend(friendId);
                friend.deleteFriend(userId);
            } else {
                throw new ValidationException(String.format("Пользователь № %d уже удален из ваших друзей", userId));
            }

    }

    public List<User> getFriends(Integer userId) {
        User user = getUserById(userId);
        return user.getFriends().stream().map(this::getUserById).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        return user.getFriends().stream()
                .filter(friend.getFriends()::contains)
                .map(this::getUserById)
                .collect(Collectors.toList());
    }
}
