package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectExcistenceException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dal.FriendStorage;
import ru.yandex.practicum.filmorate.storage.dal.UserStorage;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userDbStorage;
    private final FriendStorage friendStorage;

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
        getUserById(userId);
        User friend = getUserById(friendId);
        if (!friendStorage.getFriends(userId).contains(friend)) {
            friendStorage.addFriend(userId, friendId);
        } else {
            throw new ValidationException(String.format("Пользователь № %d уже у вас в друзьях", userId));
        }
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        getUserById(userId);
        User friend = getUserById(friendId);
        if (friendStorage.getFriends(userId).contains(friend)) {
            friendStorage.deleteFriend(userId, friendId);
        } else {
            throw new ValidationException(String.format("Пользователь № %d уже удален из ваших друзей", userId));
        }

    }

    public List<User> getFriends(Integer userId) {
        return friendStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        return friendStorage.getCommonFriends(userId, friendId);
    }
}
