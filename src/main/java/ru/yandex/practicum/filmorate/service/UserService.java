package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectExcistenceException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDbStorage userDbStorage;

    public User createUser(User user) {
        return userDbStorage.createUser(user);
    }


    public User updateUser(User user) {
        return userDbStorage.updateUser(user);
    }


    public List<User> getUsers() {
        return userDbStorage.getUsers();
    }

    public User getUserById(Integer userId) {
        return userDbStorage.getUserById(userId)
                .orElseThrow(() -> new ObjectExcistenceException("Пользователь не существует"));
    }

    public void addFriend(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        if (!userDbStorage.getFriends(userId).contains(friend) &&
                !userDbStorage.getFriends(friendId).contains(user)) {
            userDbStorage.addFriend(userId, friendId, 2);
            userDbStorage.addFriend(friendId, userId, 1);
        } else if (!userDbStorage.getFriends(userId).contains(friend) &&
                userDbStorage.getFriends(friendId).contains(user)) {
            userDbStorage.updateStatusFriendship(userId, friendId, 2);
        } else if (userDbStorage.getFriends(userId).contains(friend)) {
            throw new ValidationException(String.format("Пользователь № %d уже у вас в друзьях", userId));
        }
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        if (userDbStorage.getFriends(userId).contains(friend) &&
                userDbStorage.getFriends(friendId).contains(user)) {
            userDbStorage.updateStatusFriendship(userId, friendId, 1);
        } else if (userDbStorage.getFriends(userId).contains(friend) &&
                !userDbStorage.getFriends(friendId).contains(user)) {
            userDbStorage.deleteFriend(userId, friendId);
            userDbStorage.deleteFriend(friendId, userId);
        } else if (!userDbStorage.getFriends(userId).contains(friend)) {
            throw new ValidationException(String.format("Пользователь № %d уже удален из ваших друзей", userId));
        }

    }

    public List<User> getFriends(Integer userId) {
        return userDbStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        return userDbStorage.getCommonFriends(userId, friendId);
    }
}
